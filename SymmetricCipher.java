import java.util.HashMap;
import java.util.Map;

public class SymmetricCipher {

    public static void main(String[] args) {

        //Generate Sboxes
        Map<String, String> SBox1 = new HashMap<String, String>();
        Map<String, String> SBox2 = new HashMap<String, String>();
        generateSBox(SBox1, SBox2);

        //convert plaintext to Binary
        String plaintext = "770245FFBAD4173E";
        String plainTextBin = hexToBin(plaintext);

        String key = "12345678";
        String cipherText = "";

        //generate array to hold subkeys
        String[] subKeys = new String[10];
        generateSubkeys(subKeys, key);

        //start loop here for the rounds
        for (int i = 0; i < 10; i++) {

            //Assign first values before encryption
            String oldLeft = "";
            String oldRight = "";
            String newLeft = "";
            String newRight = "";

            //split the plaintext
            oldLeft = plainTextBin.substring(0, (plainTextBin.length() / 2));
            oldRight = plainTextBin.substring(plainTextBin.length() / 2);

            //assign newLeft
            newLeft = oldRight;

            //XOR operation with old right and key
            newRight = xOr(oldRight, subKeys[i]);

            //SBox Operation on Hex Representation
            String newRightHex = binToHex(newRight);

            String newRightAfterSbox = "";
            for (int j = 0; j < newRightHex.length(); j++) {
                if (j % 2 == 0) {
                    newRightAfterSbox += SBox1.get("" + newRightHex.charAt(j));
                } else {
                    newRightAfterSbox += SBox2.get("" + newRightHex.charAt(j));
                }
            }

            //Convert back to binary
            newRight = hexToBin(newRightAfterSbox);

            //Permute binary right string
            newRight = permutation(newRight);

            //XOr operation newRight and oldLeft
            newRight = xOr(newRight, oldLeft);

            plainTextBin = newLeft + newRight;
        }

        //swap left and right sides
        String left = plainTextBin.substring(0, plainTextBin.length() / 2);
        String right = plainTextBin.substring(plainTextBin.length() / 2);

        cipherText = right + left;
        cipherText = binToHex(cipherText);

        //code below was used for finding the key
//            if (cipherText.equals("EC2DE1305B5F5B02")) {
//                break;
//            } else {
//                BigInteger decimal = new BigInteger(key, 16);
//                decimal = decimal.add(BigInteger.ONE);
//                key = decimal.toString(16).toUpperCase();
//            }

        //System.out.println("Key: " + key);
        System.out.println("CipherText: " + cipherText);
    }

    public static void generateSBox(Map<String, String> SBox1,
            Map<String, String> SBox2) {

        SBox1.put("0", "E");
        SBox1.put("1", "4");
        SBox1.put("2", "D");
        SBox1.put("3", "1");
        SBox1.put("4", "2");
        SBox1.put("5", "F");
        SBox1.put("6", "B");
        SBox1.put("7", "8");
        SBox1.put("8", "3");
        SBox1.put("9", "A");
        SBox1.put("A", "6");
        SBox1.put("B", "C");
        SBox1.put("C", "5");
        SBox1.put("D", "9");
        SBox1.put("E", "0");
        SBox1.put("F", "7");

        SBox2.put("0", "5");
        SBox2.put("1", "6");
        SBox2.put("2", "C");
        SBox2.put("3", "F");
        SBox2.put("4", "8");
        SBox2.put("5", "A");
        SBox2.put("6", "0");
        SBox2.put("7", "4");
        SBox2.put("8", "B");
        SBox2.put("9", "3");
        SBox2.put("A", "7");
        SBox2.put("B", "D");
        SBox2.put("C", "E");
        SBox2.put("D", "1");
        SBox2.put("E", "2");
        SBox2.put("F", "9");
    }

    public static String xOr(String one, String two) {
        String xOred = "";

        for (int i = 0; i < one.length(); i++) {
            if (one.charAt(i) == two.charAt(i)) {
                xOred += "0";
            } else {
                xOred += "1";
            }
        }
        return xOred;
    }

    public static void generateSubkeys(String[] subkeys, String key) {
        String input = hexToBin(key);

        String left = input.substring(0, input.length() / 2);
        String right = input.substring(input.length() / 2);

        for (int i = 0; i < subkeys.length; i++) {
            left = left.substring(1) + left.substring(0, 1);
            right = right.substring(1) + right.substring(0, 1);
            subkeys[i] = left + right;
        }
    }

    public static String permutation(String text) {

        String permutedString = "";

        HashMap<Integer, Integer> perm = new HashMap<Integer, Integer>();
        perm.put(0, 29);
        perm.put(1, 1);
        perm.put(2, 17);
        perm.put(3, 8);

        perm.put(4, 30);
        perm.put(5, 22);
        perm.put(6, 28);
        perm.put(7, 6);

        perm.put(8, 18);
        perm.put(9, 4);
        perm.put(10, 12);
        perm.put(11, 19);

        perm.put(12, 21);
        perm.put(13, 26);
        perm.put(14, 2);
        perm.put(15, 20);

        perm.put(16, 31);
        perm.put(17, 10);
        perm.put(18, 9);
        perm.put(19, 25);

        perm.put(20, 13);
        perm.put(21, 0);
        perm.put(22, 23);
        perm.put(23, 15);

        perm.put(24, 3);
        perm.put(25, 27);
        perm.put(26, 5);
        perm.put(27, 11);

        perm.put(28, 7);
        perm.put(29, 14);
        perm.put(30, 24);
        perm.put(31, 16);

        String[] permutedArray = new String[32];

        for (int k = 0; k < 32; k++) {
            int index = perm.get(k);
            permutedArray[index] = "" + text.charAt(k);
        }
        for (String a : permutedArray) {
            permutedString += a;
        }
        return permutedString;
    }

    public static String hexToBin(String hex) {
        char[] array = hex.toCharArray();
        String value = "";

        for (char a : array) {
            switch (a) {
                case '0':
                    value += "0000";
                    break;
                case '1':
                    value += "0001";
                    break;
                case '2':
                    value += "0010";
                    break;
                case '3':
                    value += "0011";
                    break;
                case '4':
                    value += "0100";
                    break;
                case '5':
                    value += "0101";
                    break;
                case '6':
                    value += "0110";
                    break;
                case '7':
                    value += "0111";
                    break;
                case '8':
                    value += "1000";
                    break;
                case '9':
                    value += "1001";
                    break;
                case 'A':
                    value += "1010";
                    break;
                case 'B':
                    value += "1011";
                    break;
                case 'C':
                    value += "1100";
                    break;
                case 'D':
                    value += "1101";
                    break;
                case 'E':
                    value += "1110";
                    break;
                case 'F':
                    value += "1111";
                    break;
                default:
                    System.out.print("Not a valid digit");
            }
        }
        return value;
    }

    public static String binToHex(String binary) {
        String hexNum = "";
        int digitNumber = 1;
        int sum = 0;
        for (int i = 0; i < binary.length(); i++) {
            if (digitNumber == 1) {
                sum += Integer.parseInt(binary.charAt(i) + "") * 8;
            } else if (digitNumber == 2) {
                sum += Integer.parseInt(binary.charAt(i) + "") * 4;
            } else if (digitNumber == 3) {
                sum += Integer.parseInt(binary.charAt(i) + "") * 2;
            } else if (digitNumber == 4 || i < binary.length() + 1) {
                sum += Integer.parseInt(binary.charAt(i) + "") * 1;
                digitNumber = 0;
                if (sum < 10) {
                    hexNum += Integer.toString(sum);
                } else if (sum == 10) {
                    hexNum += "A";
                } else if (sum == 11) {
                    hexNum += "B";
                } else if (sum == 12) {
                    hexNum += "C";
                } else if (sum == 13) {
                    hexNum += "D";
                } else if (sum == 14) {
                    hexNum += "E";
                } else if (sum == 15) {
                    hexNum += "F";
                }
                sum = 0;
            }
            digitNumber++;
        }
        return hexNum;
    }

}