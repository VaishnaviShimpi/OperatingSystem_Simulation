import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Phase1 {
    private char[][] MEMORY = new char[100][4];  //Memory of size 100*4.
    private char[] IR = new char[4]; //Instruction Register
    private char C; //Toggle variable
    private char[] R = new char[4]; //Register
    private int IC = 0; //Instruction Counter
    private int m = 0;
    private int row_no; 
    private int SI = 3; //Sequence Interupt
    private String data;
    int s=1;

    private BufferedReader infile;
    private FileWriter outfile;

    void LOAD() throws IOException {
        int flag = 0;
        INIT();

        String line;
        String startChars="";
        while ((line = infile.readLine()) != null) {
                if (line.length() >= 4) startChars = line.substring(0, 4);
                    else startChars = line.substring(0);
                    if (startChars.equals("$END")) {
                        INIT();
                        continue;
                    } 
                    
                    else if (startChars.equals("$DTA")) {
                        START_EXECUTION();
                    }
                  
                    else if (startChars.equals("$AMJ")) {
                        flag = 0;
                        INIT();
                    }
                        else{
                            int cnt=0;
                
                            for(int i=m;i<m+10;i++){
                                for(int j=0;j<4;j++){
                                    MEMORY[i][j] = line.charAt(cnt);
                                    cnt++;
                
                                    if(cnt==line.length()) break;
                                }
                                if(cnt==line.length()) break;
                            }
                            m=m+10;
                        }

                    
                    } 
                
            
        }
 
    void READ(){
        String temp;
        data = "";
        try {
            data = infile.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data == null || data.isEmpty()) {
            System.out.println("Out Of Data \n");
            INIT();

            try {
                outfile.write("Out Of Data \n");
                outfile.write("\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            go_to_end();
        }

        while (data.length() < 4) {
            data += " "; 
        }
    
        
    
        temp = data.substring(0, 4);

        if (temp.equals("$END") || temp.isEmpty()) {
            System.out.println("Out Of Data \n");
            INIT();
           
            try {
                outfile.write("Out Of Data \n");
                outfile.write("\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (row_no > 100) {
            System.out.println("MEMORY is exceeded \n");
            System.out.println("Row no " + row_no);
            INIT();

            try {
                outfile.write("MEMORY is exceeded \n");
                outfile.write("\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            go_to_end();
        }


        int counter = 0;
  
        for (int i = row_no; counter < (data.length()); i++)
            for (int j = 0; j < 4 && counter < (data.length()); j++) {
                MEMORY[i][j] = data.charAt(counter);
                counter += 1;
                if (counter > 40)
                    break;
            }
    }


    void WRITE(){
        int count = 0;
        for (int row = row_no; count < 10; row++) {
            count++;
            for (int col = 0; col < 4; col++) {
                if (MEMORY[row][col] != '.')
                    try {
                        outfile.write(MEMORY[row][col]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        
        try {
            outfile.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void TERMINATE(){

        try {
        outfile.write("\n");
        outfile.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

       
    }

    void INIT() {
        for (int i = 0; i < 100; i++)
            for (int j = 0; j < 4; j++)
                MEMORY[i][j] = '.';

                IC = m =0;
                s=1;
    }

    void START_EXECUTION() {
        IC = 0;
        EXECUTE_USER_PROGRAM();
    }

    void EXECUTE_USER_PROGRAM() {
        for (IC = 0; IC < m; IC++) {

        String instruction=null;
       
            for (int j = 0; j < 4; j++) {
                IR[j] = MEMORY[IC][j];
            }
            instruction = "" + IR[0] + IR[1];
            row_no = (IR[2] - '0') * 10 + (IR[3] - '0');
           instruction.trim();
            if (instruction.equals("GD")) {
                SI = 1;
                MOS();
            } 
            
            else if (instruction.equals("PD")) {
                SI = 2;
                MOS();
            } 

            else if(instruction.equals("H")){
                SI = 3;
                MOS();
            }
            
            else if (instruction.equals("LR")) {

                for (int i = 0; i < 4; i++)
                    R[i] = MEMORY[row_no][i];
            } 
            
            else if (instruction.equals("SR")) {
                for (int i = 0; i < 4; i++)
                    MEMORY[row_no][i] = R[i];
            } 
            
            else if (instruction.equals("CR")) {
                int flag = 0;
                for (int i = 0; i < 4; i++)
                    if (MEMORY[row_no][i] != R[i])
                        flag = 1;
                if (flag == 0)
                    C = 'T';
                else
                    C = 'F';
                flag = 0;

            } 
            
            else if (instruction.equals("BT")) {
                if (C == 'T'){
                    IC = row_no;
                    IC--;
            }

                C = 'F';

            }
        }
    }

    void MOS() {
        switch (SI) {
            case 1: {
                READ();
                break;
            }

            case 2: {
                WRITE();
                break;
            }

            case 3:
               TERMINATE();
               break;
        }
        SI = 0;
    }

    void go_to_end() {
        String temp, file_content = "";
        while (true) {
            try {
                file_content = infile.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (file_content == null)
                break;
            temp = file_content.substring(0, 4);

            if (temp.equals("$END"))
                return;
        }
    }

    void printMemory(){
        for(int i=0;i<100;i++){
            for(int j=0;j<4;j++){
               
                System.out.print(MEMORY[i][j]+ " ");
            }
            System.out.println();
        }
    
        System.out.println("\n\n");
    }
    
    void run() throws IOException {
        infile = new BufferedReader(new FileReader("Phase1InputFile.txt"));
        outfile = new FileWriter("Phase1OutputFile.txt");
        try {
            LOAD();
            infile.close();
            outfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Phase1 myOS = new Phase1();
        myOS.run();
        System.out.println("Code Executed Successfully\n Check the Output file.");
   }
}