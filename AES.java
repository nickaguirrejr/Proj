import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class AES
{
	static int keySize = 256; // key size = 256
	static int keyN = 8;
	static int nRounds = 14; // num rounds to go for a 256 bit key
	static final boolean print = true;
	static int bytesRead = 0;

	final static int[] table = {
    0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
    0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
    0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
    0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
    0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
    0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
    0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
    0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
    0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
    0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
    0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
    0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
    0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
    0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
    0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
    0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16
 	};

 	final static int[] LogTable = {
	0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3, 
	100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193, 
	125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120, 
	101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142, 
	150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56, 
	102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16, 
	126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186, 
	43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87, 
	175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232, 
	44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160, 
	127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183, 
	204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157, 
	151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209, 
	83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171, 
	68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165, 
	103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7};

    final static int[] AlogTable = {
	1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53, 
	95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170, 
	229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49, 
	83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205, 
	76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136, 
	131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154, 
	181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163, 
	254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160, 
	251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65, 
	195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117, 
	159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 
	155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84, 
	252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202, 
	69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14, 
	18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23, 
	57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1};

	final static int[] sTable = {1};

	final static byte[][] rKey = {{1, 1, 1, 1}, {2, 2, 2, 2}, {3, 3, 3, 3}, {4, 4, 4, 4}};

    private static byte mul (int a, byte b) {
		int inda = (a < 0) ? (a + 256) : a;
		int indb = (b < 0) ? (b + 256) : b;

		if ( (a != 0) && (b != 0) ) {
		    int index = (LogTable[inda] + LogTable[indb]);
		    byte val = (byte)(AlogTable[ index % 255 ] );
		    return val;
		}
		else 
		    return 0;
    } // mul

    // In the following two methods, the input c is the column number in
    // your evolving state matrix st (which originally contained 
    // the plaintext input but is being modified).  Notice that the state here is defined as an
    // array of bytes.  If your state is an array of integers, you'll have
    // to make adjustments. 

    public static byte[][] mixColumn2 (int c, byte[][] st) {
		// This is another alternate version of mixColumn, using the 
		// logtables to do the computation.
		
		byte a[] = new byte[4];
		
		// note that a is just a copy of st[.][c]
		for (int i = 0; i < 4; i++) 
		    a[i] = st[i][c];
		
		// This is exactly the same as mixColumns1, if 
		// the mul columns somehow match the b columns there.
		st[0][c] = (byte)(mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
		st[1][c] = (byte)(mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
		st[2][c] = (byte)(mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
		st[3][c] = (byte)(mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));
		return st;
    } // mixColumn2

    public static byte[][] invMixColumn2 (int c, byte[][] st) {
		byte a[] = new byte[4];
		
		// note that a is just a copy of st[.][c]
		for (int i = 0; i < 4; i++) 
		    a[i] = st[i][c];
		
		st[0][c] = (byte)(mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]));
		st[1][c] = (byte)(mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]));
		st[2][c] = (byte)(mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]));
		st[3][c] = (byte)(mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]));
		return st;
	} // invMixColumn2

    public static String fixInput(String line){
    	if (line.length() == 16)
    		return line;
    	else if(line.length() > 16)
    		return line.substring(0, 16);
    	else{
    		while(line.length() < 16){
    			line += " ";
    		}
    		return line;
    	}
    }

	public static byte[][] encrypt(byte[][] arr, byte[][] keyMatrix, FileWriter scFile) throws IOException
	{
		int progCount = 0;
		arr = addRoundKey(arr, keyMatrix);
		while (progCount < 13){
			arr = subBytes(arr);
			arr = shiftRows(arr);
			for(int i = 0; i < arr[0].length; i++){
				arr = mixColumn2(i, arr);
			}
			arr = addRoundKey(arr, keyMatrix);
			progCount++;
		}
		arr = subBytes(arr);
		arr = shiftRows(arr);
		arr = addRoundKey(arr, keyMatrix);
		for (byte[] row: arr){
			StringBuffer sb = new StringBuffer();
			for(byte b: row){
				sb.append(String.format("%02x", b));
			}
			scFile.write(sb.toString());
		}
		scFile.write("\n");
		return arr;
	}

	public static byte[][] decrypt(byte[][] arr, byte[][] keyMatrix, FileWriter scFile) throws IOException
	{
		int progCount = 0;
		arr = addRoundKey(arr, keyMatrix);
		arr = shiftRowsBack(arr);
		arr = invSubBytes(arr);
		while (progCount < 13){
			arr = addRoundKey(arr, keyMatrix);
			for(int i = 0; i < arr[0].length; i++){
				arr = invMixColumn2(i, arr);
			}
			arr = shiftRowsBack(arr);
			arr = invSubBytes(arr);
			progCount++;
		}
		arr = addRoundKey(arr, keyMatrix);
		for (byte[] row: arr){
			StringBuffer sb = new StringBuffer();
			for(byte b: row){
				sb.append(String.format("%02x", b));
			}
			scFile.write(toStr(sb.toString()));
		}
		return arr;
	}

	public static int linearSearch(int temp){
		for (int i = 0; i < table.length; i++){
			if (table[i] == temp)
				return i;
		}
		return -1;
	}

	public static byte[][] subBytes(byte[][] arr){
		for(int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				int temp = (int)arr[i][j];
				if (temp < 0){
					temp += 256;
				}
				arr[i][j] = (byte)table[temp];
			}
		}
		return arr;
	}

	public static byte[][] invSubBytes(byte[][] arr){
		for(int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				int temp = (int)arr[i][j];
				if(temp < 0)
					temp+=256;
				int index = linearSearch(temp);
				if (index >= 128)
					index -= 256;
				arr[i][j] = (byte)index;
			}
		}
		return arr;
	}

	public static byte[][] addRoundKey(byte[][] arr, byte[][] roundKey){
		for(int i = 0; i < arr.length; i++){
			for(int j = 0; j < arr[i].length; j++){
				arr[i][j] ^= roundKey[i][j];
			}
		}
		return arr;
	}

	public static byte[][] shiftRows(byte[][] arr){
		byte temp1;
		temp1 = arr[1][0];
		arr[1][0] = arr[1][1];
		arr[1][1] = arr[1][2];
		arr[1][2] = arr[1][3];
		arr[1][3] = temp1;
		byte temp2 = arr[2][0];
		byte temp3 = arr[2][1];
		arr[2][0] = arr[2][2];
		arr[2][1] = arr[2][3];
		arr[2][2] = temp2;
		arr[2][3] = temp3;
		temp2 = arr[3][3];
		arr[3][3] = arr[3][2];
		arr[3][2] = arr[3][1];
		arr[3][1] = arr[3][0];
		arr[3][0] = temp2;
		return arr;
	}

	public static byte[][] shiftRowsBack(byte[][] arr){
		byte temp1;
		temp1 = arr[1][3];
		arr[1][3] = arr[1][2];
		arr[1][2] = arr[1][1];
		arr[1][1] = arr[1][0];
		arr[1][0] = temp1;
		byte temp2 = arr[2][2];
		byte temp3 = arr[2][3];
		arr[2][3] = arr[2][1];
		arr[2][2] = arr[2][0];
		arr[2][1] = temp3;
		arr[2][0] = temp2;
		temp1 = arr[3][0];
		arr[3][0] = arr[3][1];
		arr[3][1] = arr[3][2];
		arr[3][2] = arr[3][3];
		arr[3][3] = temp1;
		return arr;
	}


	public static byte[][] buildKeyMatrix(String scLine, byte[][] mat){
		int r = 0;
		int c = 0;
		String couplet = "";
		for(int i = 0; i < scLine.length(); i += 2){
			couplet = "0x" + scLine.substring(i, i+2);
			bytesRead++;
			mat[r][c] = (byte)(int) Integer.decode(couplet);
			c++;
			if(c == mat[0].length){
				c=0;
				r++;
			}
		}
		return mat;
	}
	
	public static byte[][] buildMatrix(String scLine, byte[][] mat){
		int r = 0;
		int c = 0;
		String couplet = "";
		for(int i = 0; i < scLine.length(); i += 2){
			couplet = "0x" + scLine.substring(i, i+2);
			mat[r][c] = (byte)(int) Integer.decode(couplet);
			c++;
			if(c == mat.length){
				c=0;
				r++;
			}
			if(r == mat.length){
				break;
			}
		}
		return mat;
	}

	public static String toHex(String line){
		String res = "";
		for(int i = 0; i < line.length(); i++){
			char ch = line.charAt(i);
			if(ch == ' '){ res += "20";}
			else{
				String hex = Integer.toHexString((int)ch);
				res+=hex;
			}
		}
		return res;
	}
	
	public static String toStr(String line){
		String res = "";
		for(int i = 0; i < line.length(); i+=2){
			String curr = "0x" + line.substring(i, i+2);
			char ch = (char)(int)Integer.decode(curr);
			res+=ch;
		}
		return res;
	}

	public static String lineNext(String scLine){
		int len = scLine.length();
		if(len <= 16){
			return "";
		}
		else{
			return scLine.substring(16, len);
		}
	}

	public static void printMatrix(byte[][] mat){
		for(int i = 0; i < mat.length; i++){
			for(int j = 0; j < mat[0].length; j++){
				System.out.print(Integer.toHexString( (int)mat[i][j] ) + " ");
			}
			System.out.print("\n");
		}
	}

	public static byte[][] expandKey(byte[][] key){
		int cols = 4 * (nRounds + 1);
		byte[][] exKey = new byte[4][cols];
		int r = 0;
		int c = 0;
		byte[][] temp = new byte[4][1];
		int[][] rcon = { {1}, {0}, {0}, {0} };
		int mod = 4;
		if(keyN == 6){mod = 6;}
		for(c = 0; c < cols; c++){
			if(c < keyN){
				exKey[0][c] = key[0][c];
				exKey[1][c] = key[1][c];
				exKey[2][c] = key[2][c];
				exKey[3][c] = key[3][c];
			}
			else{
				if(c % mod != 0){
					exKey[0][c] = (byte) (exKey[0][c-keyN] ^ exKey[0][c-1]);
					exKey[1][c] = (byte) (exKey[1][c-keyN] ^ exKey[1][c-1]);
					exKey[2][c] = (byte) (exKey[2][c-keyN] ^ exKey[2][c-1]);
					exKey[3][c] = (byte) (exKey[3][c-keyN] ^ exKey[3][c-1]);
				}
				else{
					// rotWord
					temp[0][0] = exKey[1][c-1];
					temp[1][0] = exKey[2][c-1];
					temp[2][0] = exKey[3][c-1];
					temp[3][0] = exKey[0][c-1];
					// subBytes
					temp = subBytes(temp);
					// xor with rcon
					temp[0][0] = (byte) (temp[0][0] ^ rcon[0][0]);
					temp[1][0] = (byte) (temp[1][0] ^ rcon[1][0]);
					temp[2][0] = (byte) (temp[2][0] ^ rcon[2][0]);
					temp[3][0] = (byte) (temp[3][0] ^ rcon[3][0]);
					// increment rcon for next round
					rcon[0][0] *= 2;

					exKey[0][c] = (byte) (exKey[0][c-keyN] ^ temp[0][0]);
					exKey[1][c] = (byte) (exKey[1][c-keyN] ^ temp[1][0]);
					exKey[2][c] = (byte) (exKey[2][c-keyN] ^ temp[2][0]);
					exKey[3][c] = (byte) (exKey[3][c-keyN] ^ temp[3][0]);
		}	}	}
		return exKey;	
	}

	public static void main (String [] args) throws IOException{
		// check number of command line arguments
		int argIDX = args.length;
		if(argIDX != 3 && argIDX != 4){
			System.out.println("Not enough params. Exiting.");
			System.exit(-1); // not enough arguments
		}
		argIDX = argIDX - 3;
		if(! (args[argIDX].equalsIgnoreCase("E") || args[argIDX].equalsIgnoreCase("D")) ){
			System.out.println("To encrypt, type \"E\". To decrypt, type \"E\". Exiting.");
			System.exit(-1); // invalid input
		}

		// initialize other the variables from command line
		String option = args[argIDX];
		File keyFile = new File(args[argIDX + 1]);
		String fileName = args[argIDX + 2];
		File inputFile = new File(fileName);
		if(option.equalsIgnoreCase("E")) {fileName = fileName + ".enc";}
		else {fileName = fileName + ".dec";}
		File outfile = new File(fileName);
		outfile.createNewFile();
		FileWriter fw = new FileWriter(outfile);

		// initialize inputText and key matricies
		byte[][] inputMatrix = new byte[4][4];
		byte[][] keyMatrix = new byte[4][keyN];

		Scanner scK = new Scanner(keyFile);
		String scLine = scK.next();
		keyMatrix = buildKeyMatrix(scLine, keyMatrix);
		scK.close();
		byte[][] exKeyMatrix = expandKey(keyMatrix);

		Scanner scIn = new Scanner(inputFile);
		while(scIn.hasNext()){
			scLine = scIn.nextLine();
			if(option.equalsIgnoreCase("E")){
				do{
					String temp = scLine;
					scLine = fixInput(scLine);
					scLine = toHex(scLine);
					byte[][] inputText = buildMatrix(scLine, inputMatrix);
					encrypt(inputMatrix, exKeyMatrix, fw);
					scLine = lineNext(temp);
				}while(scLine.length() != 0);
			}
			else{
				byte[][] inputText = buildMatrix(scLine, inputMatrix);
				decrypt(inputMatrix, exKeyMatrix, fw);
			}
		}
		fw.close();
	}
}

