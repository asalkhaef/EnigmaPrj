import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class EnigmaPrj {
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) throws FileNotFoundException {

        ReadFromFile read = new ReadFromFile();
        StringBuilder date = new StringBuilder(input.nextLine());
        String command = input.nextLine();
        DataSetting dataSetting = DataSetting.search(date);
        SetEnigma myEnigma = new SetEnigma();
        //set rotors and plugBoard...........
        assert dataSetting != null;
        String r1 = String.valueOf(dataSetting.rotorI);
        String r2 = String.valueOf(dataSetting.rotorII);
        String r3 = String.valueOf(dataSetting.rotorIII);
        String plug = String.valueOf(dataSetting.plugBoard);
        myEnigma.setRotor(r1, myEnigma.rotor_I);
        myEnigma.setRotor(r2, myEnigma.rotor_II);
        myEnigma.setRotor(r3, myEnigma.rotor_III);
        myEnigma.setPlugBoard(plug);
        myEnigma.setReflector();
        System.out.println(myEnigma.feedBack(command));
    }
}
class SetEnigma{
    HashMap<Character,Character> rotor_I = new HashMap<Character,Character>();
    HashMap<Character,Character> rotor_II = new HashMap<Character,Character>();
    HashMap<Character,Character> rotor_III = new HashMap<Character,Character>();
    HashMap<Character,Character> reflector = new HashMap<Character,Character>();
    HashMap<Character,Character> plugBoard = new HashMap<Character,Character>();

    void setPlugBoard(String plugStr){
        String[] plugSplited = plugStr.split(", ");
        //those that we have in file
        for(int i = 0; i < plugSplited.length; i++){
            //main part
            this.plugBoard.put(plugSplited[i].charAt(0),plugSplited[i].charAt(1));
            //reverse part
            this.plugBoard.put(plugSplited[i].charAt(1),plugSplited[i].charAt(0));
        }
        //those that we don't have
        for(int j = 0; j < 26; j++){
            char thisChar = (char)(97+j);
            this.plugBoard.putIfAbsent(thisChar,thisChar);
        }
    }

    void setReflector(){
        for(int k = 0; k < 26; k++){
            char charOne = (char)(97 + k);
            char charTwo = (char)(122 - k);
            this.reflector.put(charOne,charTwo);
        }
    }

    HashMap<Character,Character> setRotor(String rotStr, HashMap<Character,Character> hashMap){
        for (int i = 0; i < rotStr.length(); i++) {
            char thisChar = (char)(97 + i);
            hashMap.put(thisChar, rotStr.charAt(i));
        }
        return hashMap;
    }

    HashMap<Character, Character> setReverseRotor(HashMap<Character,Character> hashMap) {
        HashMap<Character,Character> reversedRotor = new HashMap<>();
        for (int i = 0; i < hashMap.size(); i++) {
            char thisChar = (char)(97 + i);
            reversedRotor.put(hashMap.get(thisChar), thisChar);
        }
        return reversedRotor;
    }

    String shiftRotor(String rotStr){
        char firstChar = rotStr.charAt(rotStr.length()-1);
        String otherChars = rotStr.substring(0,rotStr.length()-1);
        return firstChar + otherChars;
    }

    String hashMap_String(HashMap<Character,Character> hashMap) {
        StringBuilder converted = new StringBuilder();
        for (int i = 0; i < hashMap.size(); i++) {
            char thisChar = (char)(97 + i);
            converted.append(hashMap.get(thisChar));
        }
        return converted.toString();
    }

    StringBuilder feedBack (String readFile){
        StringBuilder feedBack = new StringBuilder();
        int countRotorII = 0;
        int countRotorIII = 0;
        for(int k = 0; k < readFile.length(); k++){
            char thisChar;
            thisChar = plugBoard.get(readFile.charAt(k));
            thisChar = rotor_III.get(thisChar);
            thisChar = rotor_II.get(thisChar);
            thisChar = rotor_I.get(thisChar);
            thisChar = reflector.get(thisChar);
            thisChar = setReverseRotor(rotor_I).get(thisChar);
            thisChar = setReverseRotor(rotor_II).get(thisChar);
            thisChar = setReverseRotor(rotor_III).get(thisChar);
            thisChar = plugBoard.get(thisChar);
            feedBack.append(thisChar);
            String convert3 = hashMap_String(rotor_III);
            String shift3 = shiftRotor(convert3);
            setRotor(shift3,rotor_III);
            countRotorIII++;

            if (countRotorIII % 26 == 0) {
                String convert2 = hashMap_String(rotor_II);
                String shift2 = shiftRotor(convert2);
                setRotor(shift2,rotor_II);
                countRotorII++;
            }
            if (countRotorII % 26 == 0 && countRotorII != 0) {
                String convert1 = hashMap_String(rotor_I);
                String shift1 = shiftRotor(convert1);
                setRotor(shift1,rotor_I);
            }
        }
        return feedBack;
    }
}

class DataSetting{

    StringBuilder date;
    StringBuilder plugBoard;
    StringBuilder rotorI;
    StringBuilder rotorII;
    StringBuilder rotorIII;

    static ArrayList<DataSetting> dataSetting = new ArrayList<>();

    public DataSetting(StringBuilder date, StringBuilder plugBoard, StringBuilder rotorI, StringBuilder rotorII, StringBuilder rotorIII) {
        this.date = date;
        this.plugBoard = plugBoard;
        this.rotorI = rotorI;
        this.rotorII = rotorII;
        this.rotorIII = rotorIII;
    }
    //searching for specific setting in file
    static DataSetting search(StringBuilder date){
        for (int i = 0; i < dataSetting.size(); i++) {
            if (dataSetting.get(i).date.compareTo(date) == 0){
                return dataSetting.get(i);
            }
        }
        return null;
    }
}
class ReadFromFile
{
    File file = new File("EnigmaFile.txt");
    private Scanner read;
    {
        try {
            read = new Scanner(file);
            while (read.hasNextLine()){
                // read date
                read.next(); //pass until space
                StringBuilder fileDate = new StringBuilder(read.nextLine());
                fileDate.deleteCharAt(0); //delete space

                //read plugBoard
                read.next();  //pass until space
                StringBuilder filePlugBoard = new StringBuilder(read.nextLine());
                filePlugBoard.deleteCharAt(0); //delete space
                filePlugBoard.deleteCharAt(0); //delete [
                filePlugBoard.deleteCharAt(filePlugBoard.length()-1); //delete ]

                // read rotorI
                read.next();  //pass until space
                StringBuilder fileRotorI = new StringBuilder(read.nextLine());
                fileRotorI.deleteCharAt(0); //delete space
                fileRotorI.deleteCharAt(0); //delete [
                fileRotorI.deleteCharAt(fileRotorI.length()-1); //delete ]

                //read rotorII
                read.next();  //pass until space
                StringBuilder fileRotorII = new StringBuilder(read.nextLine());
                fileRotorII.deleteCharAt(0); //delete space
                fileRotorII.deleteCharAt(0); //delete [
                fileRotorII.deleteCharAt(fileRotorII.length()-1); //delete ]

                //read rotorIII
                read.next();  //pass until space
                StringBuilder fileRotorIII = new StringBuilder(read.nextLine());
                fileRotorIII.deleteCharAt(0); //delete space
                fileRotorIII.deleteCharAt(0); //delete [
                fileRotorIII.deleteCharAt(fileRotorIII.length()-1); //delete ]

                //add new setting to arrayList
                DataSetting newSetting = new DataSetting(fileDate,filePlugBoard,fileRotorI,fileRotorII,fileRotorIII);
                DataSetting.dataSetting.add(newSetting);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
