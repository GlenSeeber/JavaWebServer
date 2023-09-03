import java.io.*;

public class test {
    public static void main(String args[]){
        FileInputStream fileStream = null;    
        int maxValue = 0;
        OutputStream output = null;
        //assign files n such
            try{
                File resource = new File("files/TrinitySite.jpg");
                File copy = new File("files/TrinitySite2.jpg");
                fileStream = new FileInputStream(resource);
                output = new BufferedOutputStream(new FileOutputStream(copy));
                maxValue = fileStream.available();
        
            } catch(FileNotFoundException e){
                System.out.println(e);
            } catch (Exception e){
                System.out.println(e);
            }  
            //byte array
            byte[] outBuff = new byte[maxValue];
            try{
                int n;
                while((n = fileStream.read(outBuff) ) > 0){
                    output.write(outBuff, 0, n);
                    output.flush();
                }

            } catch (Exception e){
                System.out.println(e);
            }


    }
}
