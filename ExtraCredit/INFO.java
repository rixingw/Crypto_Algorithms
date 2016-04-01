import java.io.*;
import java.util.*;
import java.math.BigInteger;


public class INFO implements Serializable {
	private String info_name;
	private String message;
	private Serializable data = null;

	public INFO(String name, String m) {
		this.info_name = name;
		this.message = m;
	}

	public Serializable getData() {
		return data;
	}
	public void setData(Serializable d) {
		data = d;
	}


	public String getMessage(){
		return message;
	}

	public void printInfo() {
		System.out.println("Name: " + info_name + " Value: " + message);
	}

	public String toString(){
		return info_name + ": " + message;
	}
}
