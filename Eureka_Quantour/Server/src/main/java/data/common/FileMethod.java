package data.common;

import java.io.File;
import java.util.UUID;

/**
 * 涉及到文件操作的公用方法
 * @author 刘宇翔
 *
 */
public class FileMethod {
	private static FileMethod p;
	private FileMethod(){
	}
	public static FileMethod getInstance(){
		if(p==null) p=new FileMethod();
		return p;
	}
	/**
	 * 删除文件夹下的所有文件
	 * @param file 文件夹
	 */
	public void dealdir(File file){
		File[] filelist=file.listFiles();
		for(File i:filelist){
			if(i.isDirectory())
			{
				for(File j:i.listFiles())
				{
					j.delete();
				}
			}
			i.delete();
		}
	}
	/**
	 * 生成文件夹
	 * @param filepath 文件夹路径
	 */
	public void makepath(String filepath){
		File file=new File(filepath);
		if(!file.exists()&&!file.isDirectory()){
			file.mkdirs();
		}
	}
	public String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString(); 
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }
}
