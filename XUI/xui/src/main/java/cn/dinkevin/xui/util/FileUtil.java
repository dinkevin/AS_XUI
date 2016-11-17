package cn.dinkevin.xui.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;


/**
 * 文件操作相关工具类
 */
public class FileUtil {

	private FileUtil() {}
	
	/**
	 * 创建文件夹或者文件，根据是否存在后缀来判断是否为文件路径
	 * @param path 如果传入的路径存在后缀格式为认定为文件路径，否则认定为目录路径
	 * @return
	 */
	public static boolean create(String path){
		
		String suffix = getFileSuffix(path);
		if(StringUtil.isEmpty(suffix)){
			return createDir(path);
		}
		
		String separator = File.separator;
		int index = path.lastIndexOf(separator);
		if(index > 0){
			
			String dir = path.substring(0,index);
			if(!createDir(dir)){
				return false;
			}
		}
		
		return createFile(path);
	}
	
	/**
	 * 重建文件夹或者文件，根据是否存在后缀来判断是否为文件路径，先删除后重建。
	 * @param path 如果传入为目录则创建目录，如果传入为文件路径则创建文件
	 * @return true 表示重建成功，否则失败
	 */
	public static boolean recreate(String path){
		FileUtil.delete(path);
		return create(path);
	}
	
	/**
	 * 创建目录，如果目录已经存在则直接返回
	 * @param dirPath
	 * @return true -> 目录创建成功、目录已经存在;false -> 目录创建失败
	 */
	public static boolean createDir(String dirPath){
		File file = new File(dirPath);
		if(!file.exists())
			return file.mkdirs();
		return true;
	}
	
	/**
	 * 创建文件
	 * @param filePath
	 */
	public static boolean createFile(String filePath){
		File file = new File(filePath);
		
		if(!file.exists()){
			try {
				return file.createNewFile();
			} catch (IOException e) {
				LogUtil.e("创建文件失败 "+filePath);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检测目标文件是否存在
	 * @param filePath 路径
	 * @return true->存在；否则表示不存在
	 */
	public static boolean exist(String filePath){
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 检测目标文件是否存在 
	 * @param dirPath 目录
 	 * @param fileName 文件名称
	 * @return true->存在；否则表示不存在
	 */
	public static boolean exist(String dirPath, String fileName){
		File file = new File(dirPath,fileName);
		return file.exists();
	}
	
	/**
	 * 删除指定目录、文件
	 * @param path 目录或者文件路径
	 * @return
	 */
	public static void delete(String path){
		File file = new File(path);
		if(file.exists()){
			if(file.isFile()){
				file.delete();
			}else{
				String parentPath = file.getAbsolutePath();
				String[] subFile = file.list();
				if(subFile == null || subFile.length == 0){ // 子文件列表为空则删除空目录
					file.delete();
				}
				else{
					for(String name : subFile){
						String filePath = parentPath + File.separator + name;
						delete(filePath);
					}
				}
			}
		}
	}
	
	/**
	 * 获取文件后缀名称
	 * @param filePath 文件路径
	 * @return
	 */
	public static String getFileSuffix(String filePath){
		if(TextUtils.isEmpty(filePath)) return null;
		int index = filePath.lastIndexOf(".");
		if(index > 0 && index + 1 < filePath.length() - 1){
			return filePath.substring(index + 1);
		}
		return null;
	}
	
	/**
	 * 获取文件内容
	 * @param filePath 文件路径
	 * @param buffer 缓冲区
	 * @param skipCount 文件读取起始位置
	 * @return 已读数据长度
	 */
	public static int readFileData(String filePath, byte[] buffer, int skipCount){
		InputStream input = openFileInputStream(filePath);
		if(input != null){
			int count = -2;
			try {
				input.skip(skipCount);
				count = input.read(buffer);
			} catch (IOException e) {
				LogUtil.e("读取文件内容异常",e.getMessage());
				closeStream(input);
				return -1;
			}
			
			closeStream(input);
			if(count > -1){
				byte[] data = new byte[count];
				System.arraycopy(buffer, 0, data, 0, count);
				return count;
			}
		}
		return -1;
	}
	
	/**
	 * 打开文件输入流
	 * @param filePath
	 * @return 文件输入流或者null（如果文件不存在或者打开输入流失败）
	 */
	public static InputStream openFileInputStream(String filePath){
		File file = new File(filePath);
		if(file.exists() && file.canRead()){
			try {
				FileInputStream input = new FileInputStream(file);
				return input;
			} catch (FileNotFoundException e) {
				LogUtil.e("打开文件输入流失败",filePath);
			}
		}
		return null;
	}
	
	/** 
	 * 打开文件输出流
	 * @param filePath
	 * @return 文件输出流或者null（如果文件不存在或者打开输出流失败）
	 */
	public static OutputStream openFileOutputStream(String filePath){
		File file = new File(filePath);
		if(file.exists() && file.canWrite()){
			try {
				FileOutputStream output = new FileOutputStream(file);
				return output;
			} catch (FileNotFoundException e) {
				LogUtil.e("打开文件输出流失败",filePath, Log.getStackTraceString(e));
			}
		}
		return null;
	}

	/**
	 * 将 InputStream 输出到 StringBuffer 缓存，</br>
	 * 注意如果此 InputStream 中的数据过长，则可能出现内存溢出问题
	 * @param input
	 * @return
     */
	public static StringBuffer printToBuffer(InputStream input){
		StringBuffer buffer = new StringBuffer();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		try{
			while ((line = reader.readLine()) != null){
				buffer.append(line);
			}
		}catch (IOException e){
			LogUtil.e("printToBuffer", Log.getStackTraceString(e));
		}
		return buffer;
	}
	
	/**
	 * 追加数据到当前指定的文件中
	 * @param data 待写入的数据
	 * @param byteOffset data 起始位置
	 * @param byteCount data 要写入的长度
	 * @param filePath 目标文件路径
	 * @return true -> 写入成功； false -> 写入失败
	 */
	public static boolean appendToFile(byte[] data,int byteOffset,int byteCount,String filePath){
		
		File f = new File(filePath);
		if(!create(filePath)){
			LogUtil.e("创建文件失败",filePath);
			return false;
		}
		
		RandomAccessFile file;
		try {
			file = new RandomAccessFile(f, "rw");
			long seekPosition = file.length();
			file.seek(seekPosition);
			file.write(data, byteOffset, byteCount);
			file.close();
			return true;
		} catch (Exception e) {
			LogUtil.e("写入数据到",filePath,"异常", Log.getStackTraceString(e));
			return false;
		}
	}
	
	/**
	 * 向指定的路径写入数据
	 * @param data 数据
	 * @param offset 数据写入开始位置
	 * @param count 数据写入长度
	 * @param filePath 目标路径
	 * @return true -> 写入成功;false -> 写入失败
	 */
	public static boolean writeToFile(byte[] data,int offset,int count,String filePath){
		
		if(!FileUtil.create(filePath)){
			LogUtil.e("创建文件失败",filePath);
			return false;
		}
		
		OutputStream output = openFileOutputStream(filePath);
		if(output != null){
			try {
				output.write(data, offset, count);
			} catch (IOException e) {
				LogUtil.e("写入文件失败",filePath, Log.getStackTraceString(e));
				closeStream(output);
				return false;
			} finally {
				closeStream(output);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 关闭流
	 * @param stream
	 */
	public static void closeStream(Closeable stream){
		if(null == stream) return;
		try {
			stream.close();
		} catch (IOException e) {
			LogUtil.e("关闭流异常",e.getMessage());
		}
	}

	public static byte[] readToBuffer(String filePath){
		InputStream input = openFileInputStream(filePath);
		if(null != input){
			int length = (int)getFileLength(filePath);
			byte[] buffer = new byte[length];
			byte[] temp = new byte[1024];
			int readCount;
			int point = 0;
			try{
				while ((readCount = input.read(temp)) != -1){
					System.arraycopy(temp,0,buffer,point,readCount);
					point += readCount;
				}
				return buffer;
			}catch (IOException e){
				LogUtil.e("read", Log.getStackTraceString(e));
			}finally {
				closeStream(input);
			}
		}
		return null;
	}
	
	/**
	 * 将输入流写入到指定的文件中，写入结束后，别忘记关闭处理输入流
	 * @param input 输入流
	 * @param filePath 存储目标文件路径
	 * @return -1 表示文件写入过程中异常；>= 0 表示写入数据长度
	 */
	public static long writeToFile(InputStream input, String filePath){
		if(create(filePath)){
			OutputStream output = openFileOutputStream(filePath);
			if(null != output){
				byte[] buffer = new byte[1024];
				int readLength = 0;
				long count = 0;
				try {
					while((readLength = input.read(buffer)) != -1){
						output.write(buffer,0,readLength);
						count += readLength;
					}
				} 
				catch (IOException e) {
					LogUtil.e("读取输入流异常", Log.getStackTraceString(e));
					return -1;
				}
				finally {
					closeStream(output);
				}
				return count;
			}
		}
		return -1;
	}
	
	/**
	 * 读取指定文件长度
	 * @param filePath
	 * @return 如果此文件不存在、不可读、文件夹则返回 -1；否则返回文件长度
	 */
	public static long getFileLength(String filePath){
		File file = new File(filePath);
		if(file.exists() && file.canRead() && file.isFile()){
			return file.length();
		}
		return -1;
	}
	
	/**
	 * 获取指定目录下的文件、文件夹列表
	 * @param dirPath 目录路径
	 * @return null 或者 文件、文件夹列表
	 */
	public static File[] list(String dirPath){
		File file = new File(dirPath);
		if(file.isDirectory()){
			return file.listFiles();
		}
		return null;
	}
	
	/**
	 * 获取指定目录下文件的总大小
	 * @param dirPath
	 * @return
	 */
	public static long getDirFileLength(String dirPath){
		long size = 0;
		File file = new File(dirPath);
		if(file.isDirectory())
		{
			File[] subFiles = file.listFiles();
			if(null != subFiles)
			{
				for(File f : subFiles){
					if(f.isDirectory()){
						getDirFileLength(f.getAbsolutePath());
					}
					else{
						size += f.length();
					}
				}
			}
		}
		return size;
	}
	
	/**
	 * 将 byte 转化到 MB 显示
	 * @param size 单位为byte表示的大小
	 * @return
	 */
	public static String byteSizetoMBSize(long size){
		if(size < 0){
			LogUtil.e("byteSize 大小需大于0",size);
			return null;
		}
		
		BigDecimal unit = new BigDecimal(1024);
		BigDecimal byteSize = new BigDecimal(size);
		BigDecimal KBSize = byteSize.divide(unit);
		BigDecimal MBSize = KBSize.divide(unit);
		return Double.toString(MBSize.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
}
