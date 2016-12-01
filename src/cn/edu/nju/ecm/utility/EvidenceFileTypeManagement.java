package cn.edu.nju.ecm.utility;

public class EvidenceFileTypeManagement {

	public enum EvidenceFileType{
		Text, Word, Video, Image, Voice, Others
	}
	
	private static String[] text = {"txt"};
	private static String[] word = {"doc", "docx"};
	private static String[] video = {"rmvb", "avi", "mp4"};
	private static String[] image = {"jpg", "jpeg", "png"};
	private static String[] voice = {"mp3", "wav", "wma"};
	
	public static EvidenceFileType decodeFromExtension(String fileExtension){
		if(hasThisExtension(text, fileExtension)){
			return EvidenceFileType.Text;
		}
		if(hasThisExtension(word, fileExtension)){
			return EvidenceFileType.Word;
		}
		if(hasThisExtension(video, fileExtension)){
			return EvidenceFileType.Video;
		}
		if(hasThisExtension(image, fileExtension)){
			return EvidenceFileType.Image;
		}
		if(hasThisExtension(voice, fileExtension)){
			return EvidenceFileType.Voice;
		}
		return EvidenceFileType.Others;
	}
	
	public static boolean hasThisExtension(String[] types, String extension){
		for(String type : types){
			if(type.equals(extension)){
				return true;
			}
		}
		return false;
	}
	
	public static EvidenceFileType decodeDirectly(String type){
		if(type.equals(EvidenceFileType.Text + "")){
			return EvidenceFileType.Text;
		}
		if(type.equals(EvidenceFileType.Word + "")){
			return EvidenceFileType.Word;
		}
		if(type.equals(EvidenceFileType.Video + "")){
			return EvidenceFileType.Video;
		}
		if(type.equals(EvidenceFileType.Image + "")){
			return EvidenceFileType.Image;
		}
		if(type.equals(EvidenceFileType.Voice + "")){
			return EvidenceFileType.Voice;
		}
		if(type.equals(EvidenceFileType.Others + "")){
			return EvidenceFileType.Others;
		}
		return EvidenceFileType.Others;
	}
	
}
