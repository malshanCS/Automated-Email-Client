//200304N

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.*;

//Abstract class for Recipients
abstract class Recipient {
    private String name;
    private String email;

    public Recipient(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//this interface will only be implemented by personal and office friends.
interface iMessage {

    void setMessage(String message);

    String getMessage();
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//Class for Personal recipients
class Personal_Recipient extends Recipient implements iMessage{
    private String nickName;
    private String birthday;

    private String message = "hugs and love on your birthday. Malshan";

    public Personal_Recipient(String name, String email) {
        super(name, email);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//Class for Official recipients
class Official_Recipient extends Recipient {

    private String designation;

    public Official_Recipient(String name, String email) {
        super(name, email);
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//Class for Office friends recipients
class OfficeFriend_Recipient extends Recipient implements iMessage{

    private String designation;
    private String message = "Wish you a Happy Birthday. Malshan";
    private String birthday;

    public OfficeFriend_Recipient(String name, String email) {
        super(name, email);
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This Factory class will create the recipients based on the type of recipient
class RecipientFactory {

    public Recipient getRecipients(String line){
        String[] temp1;
        String[] temp2;
        temp1 = line.strip().split(":"); //separated from : sign
        var type = temp1[0].toLowerCase();

        if(type.equals("personal")){     //Personal: bimal,bima,malshanacc@gmail.com,2000/10/10
            var details = temp1[1];
            temp2 = details.strip().split(",");

            if(emailList.isExist(temp2[2])){
                throw new InvalidParameterException("Multiple Recipients with the same Email: " + temp2[2] + " found. Only the first recipient was added");
            } else if (temp2.length != 4) {
                throw new InvalidParameterException("Given number of inputs does not match the recipient type");

            } else{
                emailList.addEmail(temp2[2]);
                var rec = new Personal_Recipient(temp2[0], temp2[2]);
                rec.setBirthday(temp2[3]);
                rec.setNickName(temp2[1]);
                return rec;
            }

        } else if (type.equals("office_friend")) { //Office_friend:jayan,malshankeerthi@gmail.com,clerk,2000/08/11
            var details = temp1[1];
            temp2 = details.strip().split(",");


            if(emailList.isExist(temp2[1])){
                throw new InvalidParameterException("Multiple Recipients with the same Email: " + temp2[1] + " found. Only the first recipients was added");
            }else if (temp2.length !=4) {
                throw new InvalidParameterException("Given number of inputs does not match the recipient type");
            }
            else{
                emailList.addEmail(temp2[1]);
                var rec = new OfficeFriend_Recipient(temp2[0], temp2[1]);
                rec.setBirthday(temp2[3]);
                rec.setDesignation(temp2[2]);
                return rec;
            }


        }
        else if (type.equals("official")) { //Official:nimal,nimal@gmail.com,ceo
            var details = temp1[1];
            temp2 = details.strip().split(",");

            if(emailList.isExist(temp2[1])){
                throw new InvalidParameterException("Multiple Recipients with the same Email: " + temp2[1] + " found. Only the first recipients was added");
            } else if (temp2.length !=3) {
                throw new InvalidParameterException("Given number of inputs does not match the recipient type");

            } else{
                emailList.addEmail(temp2[1]);
                var rec = new Official_Recipient(temp2[0], temp2[1]);
                rec.setDesignation(temp2[2]);
                return rec;
            }


        }
        else if (type.equals("")) {
            throw new InvalidParameterException("Empty line found");
        }

        else{throw new IllegalArgumentException("Unknown Recipient Type :"+ type);
        }
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will store each email address of recipients and check whether
// the email address is already in the clientList.txt
class emailList {
    public static List<String> emailList = new ArrayList<>();

    public static void addEmail(String email){
        emailList.add(email);
    }

    public static boolean isExist(String email){
        return emailList.contains(email);
    }

}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will store each recipient in the list
class RecipientsStorage {

    private static List<Recipient> recipientList;

    public static List<Recipient> getRecipientList() {
        return recipientList;
    }

    public static void setRecipientList(List<Recipient> recipientList) {
        RecipientsStorage.recipientList = recipientList;
    }


    public void addRecipient(Recipient recipient){
        recipientList.add(recipient);
    }

    public int getSize(){
        return recipientList.size();
    }
}


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class provides getDetails functionality for maps
abstract class getDetails {
    public void getdetails(String date){}

}


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will store the recipients birthday as the key.
class RecipientMap extends getDetails implements Serializable {

    private static HashMap<String, List<Recipient>> map;

    public static HashMap<String, List<Recipient>> getMap() {
        return map;
    }

    public static void setMap(HashMap<String, List<Recipient>> map) {
        RecipientMap.map = map;
    }

    public void addRecipient(String birthday, Recipient recipient){

        if(map.containsKey(birthday)){map.get(birthday).add(recipient);}

        else{
            List<Recipient> list = new ArrayList<>();
            list.add(recipient);
            map.put(birthday,list);
        }
    }

    public void getdetails(String date){
        if(map.containsKey(date)){
            var list = map.get(date);

            for(Recipient recipient: list){
                System.out.println(recipient.getName());
            }
        }
        else{throw new InvalidParameterException("Invalid input");}

    }

}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//HashMap to store details about the emails sent in a particular day
class DescriptionMap extends getDetails {
    private static HashMap<String, List<String>> map;

    public static HashMap<String, List<String>> getMap() {
        return map;
    }

    public static void setMap(HashMap<String, List<String>> map) {
        DescriptionMap.map = map;
    }


    public void addDescription(String date, String details){

        if(map.containsKey(date)){map.get(date).add(details);}

        else{
            List<String> list = new ArrayList<>();
            list.add(details);
            map.put(date,list);
        }
    }
    @Override
    public void getdetails(String date) {
        if(map.containsKey(date)){
            var list = map.get(date);

            for(String details: list){
                System.out.println(details);
            }
        }
        else{throw new InvalidParameterException("No mails were sent");}

    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will add information to DescriptionMap and RecipientMap
class mapAdder {
    public RecipientMap birthdayMap = new RecipientMap();
    public DescriptionMap descriptionMap = new DescriptionMap();


    void addMembers(Recipient recipient){
        String birthday = null;
        if((recipient instanceof Personal_Recipient personal)){
            String tempBirthday = personal.getBirthday();
            birthday = tempBirthday.substring(5);


        } else if (recipient instanceof OfficeFriend_Recipient officeFriendRecipient) {
            String tempBirthday = officeFriendRecipient.getBirthday();
            birthday = tempBirthday.substring(5);

        }else if (recipient instanceof Official_Recipient official_recipient){
            {}
        }

        else{throw new InvalidParameterException("Unknown Recipient Type");
        }

        birthdayMap.addRecipient(birthday,recipient);
    }


    void getMembers(String date){
        String birthday = date.substring(5);
        try{
            birthdayMap.getdetails(birthday);
        }catch (InvalidParameterException e){
            System.out.println("No recipients have birthdays this day");
        }

    }

    void addDetails(String Date, String details){
        descriptionMap.addDescription(Date,details);
    }

    void getDetails(String date){
        try{
            descriptionMap.getdetails(date);
        } catch (InvalidParameterException e){
            System.out.println("No mails were sent");
        }
    }


}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will handle the email sending process as a broker between SendEmailTLS
//and the Client
class EmailContext {

    private SendEmailTLS sender = new SendEmailTLS();


    public void SendMessage(Recipient recipient, String date) {
        String message;
        String subject = "Birthday Wishes!!";
        String birthday;

        if(recipient instanceof Personal_Recipient){
            message = ((Personal_Recipient) recipient).getMessage();
            birthday = ((Personal_Recipient) recipient).getBirthday();

            if(Objects.equals(date, birthday.substring(5))){
                sendMail(recipient.getEmail(), subject, message);}


        } else if (recipient instanceof OfficeFriend_Recipient) {
            message = ((OfficeFriend_Recipient) recipient).getMessage();
            birthday = ((OfficeFriend_Recipient) recipient).getBirthday();
            if(Objects.equals(date, birthday.substring(5))){
                sendMail(recipient.getEmail(), subject, message);}

        }
        else if (recipient instanceof Official_Recipient) {
            {}
        }
        else{throw new InvalidParameterException("Unknown Recipient Type"+recipient.getClass().getName());
        }
    }

    public void sendMail(String email, String subject, String content){
        try {
            sender.sendEmail(content, email, subject);
        } catch (Exception e) {
            e.printStackTrace();}

    }

}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will send the email to the recipients
class SendEmailTLS {
    mapAdder mapAdder = new mapAdder();
    java.util.Date date = new Date();
    String dateString = date.toString();
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");

    String Date = formatter1.format(date);

    private final String usermail = "malshanacc@gmail.com"; //= "username@gmail.com"; //change these   "malshanacc@gmail.com"
    private final String password = "lbpymhnlsyrazbae"; // = "password";//change these  "lbpymhnlsyrazbae"


    public void sendEmail(String emailToSend, String recieverAddress, String mailSubject) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usermail, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usermail));//change these
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recieverAddress)//change these
            );

            message.setSubject(mailSubject);//change these
            message.setText(emailToSend);//change these

            Transport.send(message);
            System.out.println("Email sent successfully to " + recieverAddress);

            String details = "Subject: " + mailSubject + "   Content: " + emailToSend +  "   sent to: " + recieverAddress;
            mapAdder.addDetails(Date,details);


        } catch (MessagingException e) {
            System.out.println("Sending message failed for " + recieverAddress +  " Recheck the email address");
        }
    }
}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//this class will be used to store the details of sent emails of recipients only in each date
class sentMails implements Serializable {

    private static HashMap<String, List<String>> sentMap;

    public static HashMap<String, List<String>> getSentMap() {
        return sentMap;
    }

    public static void setSentMap(HashMap<String, List<String>> sentMap) {
        sentMails.sentMap = sentMap;
    }

    public void addItems(String date, String email){

        if(sentMap.containsKey(date)){sentMap.get(date).add(email);}

        else{
            ArrayList<String> list = new ArrayList<>();
            list.add(email);
            sentMap.put(date,list);
        }
    }

    public boolean checkSent(String date, String email){
        if(sentMap.containsKey(date)){
            var list = sentMap.get(date);
            return list.contains(email);
        }
        else{return false;}
    }

}
///++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//This class will handle serializing/deserializing process
class SerializeDeserialize {


    public static void Serialize(HashMap<String, List<String>> map1, HashMap<String, List<String>> map2) throws IOException {
        FileOutputStream fos1 = new FileOutputStream("sentMap.ser", false);
        FileOutputStream fos2 = new FileOutputStream("detailsMap.ser", false);
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);

        oos1.writeObject(map1);
        oos2.writeObject(map2);
        oos1.close();
        oos2.close();



    }


    public static HashMap<String, List<String>> DeserializeMap1() throws IOException, ClassNotFoundException {
        //deserialize the map
        FileInputStream fis = new FileInputStream("sentMap.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        HashMap<String, List<String>> mapobj1 = (HashMap<String, List<String>>) ois.readObject();
        ois.close();
        return mapobj1;
    }


    public static HashMap<String, List<String>> DeserializeMap2() throws IOException, ClassNotFoundException {
        //deserialize the map
        FileInputStream fis2 = new FileInputStream("detailsMap.ser");
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        HashMap<String, List<String>> mapobj2 = (HashMap<String, List<String>>) ois2.readObject();
        ois2.close();
        return mapobj2;
    }
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class Email_Client {
    public static void main(String[] args) throws IOException {
        var factory = new RecipientFactory();
        var storage = new RecipientsStorage();
        var addToMap = new mapAdder();
        var sendmail = new sentMails();
        var mailSender = new EmailContext();

        HashMap<String, List<Recipient>> birthdayMap = new HashMap<>();
        RecipientMap.setMap(birthdayMap);

        List<Recipient> recipientList = new ArrayList<>();
        RecipientsStorage.setRecipientList(recipientList);

        //create maps that need to be serialized
        HashMap<String, List<String>> DetailsMap;

        HashMap<String, List<String>> sentMailsMap;

        //deserialize the maps
        try{
            sentMailsMap = SerializeDeserialize.DeserializeMap1();
        }
        catch(ClassNotFoundException | IOException e){
            sentMailsMap = new HashMap<>();
        }

        try{
            DetailsMap = SerializeDeserialize.DeserializeMap2();
        }
        catch(ClassNotFoundException | IOException e){
            DetailsMap = new HashMap<>();
        }


        sentMails.setSentMap(sentMailsMap);
        DescriptionMap.setMap(DetailsMap);


        //get current date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        String day = formatter.format(date);



        //Read the file line by line
        File ClientFile = new File("clientList.txt");

        try{
            if(!ClientFile.createNewFile()) {
                Scanner reader = new Scanner(ClientFile);

                while(reader.hasNextLine()){
                    var clientLine = reader.nextLine();

                    try{
                        Recipient recipient = factory.getRecipients(clientLine);
                        storage.addRecipient(recipient);
                        addToMap.addMembers(recipient);




                    }catch (InvalidParameterException e){
                        System.out.println(e);
                    }
                }
                reader.close();
            }

        } catch (IOException e){e.printStackTrace();
        }

        //send email for each recipient having birthday on the current day
        for(Recipient recipient: RecipientsStorage.getRecipientList()){
            if(!sendmail.checkSent(day, recipient.getEmail())){
                mailSender.SendMessage(recipient,day);
                sendmail.addItems(day, recipient.getEmail());
            }
        }



        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter option type: \n"
                + "1 - Adding a new recipient\n"
                + "2 - Sending an email\n"
                + "3 - Printing out all the recipients who have birthdays\n"
                + "4 - Printing out details of all the emails sent\n"
                + "5 - Printing out the number of recipient objects in the application");
        try{
            int option = scanner.nextInt();
            scanner.nextLine();

            switch(option){
                case 1:
                    // input format - Official: nimal,nimal@gmail.com,ceo
                    // Use a single input to get all the details of a recipient
                    // code to add a new recipient
                    // store details in clientList.txt file
                    // Hint: use methods for reading and writing files

                    String lineTemp = scanner.nextLine();
                    String line = lineTemp.replaceAll(" ","");
                    try{
                        var inputRecipient = factory.getRecipients(line);
                        storage.addRecipient(inputRecipient);
                        addToMap.addMembers(inputRecipient);
                        var fileWriter = new FileWriter("clientList.txt", true);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        writer.write(line);
                        writer.newLine();
                        writer.close();
                        System.out.println("recipient added");

                    } catch (IndexOutOfBoundsException e){
                        System.out.println("Given number of inputs does not match the recipient type");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e);
                    }

                    break;
                case 2:
                    // input format - email, subject, content
                    // code to send an email
                    String line2 = scanner.nextLine();
                    String[] temp3 = line2.split(",");

                    if(temp3.length != 3){
                        throw new IllegalArgumentException("Invalid number of inputs");
                    }
                    try {
                        mailSender.sendMail(temp3[0], temp3[1], temp3[2]);
                    }catch (InvalidParameterException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input");
                    }
                    break;
                case 3:
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print recipients who have birthdays on the given date
                    String line1 = scanner.nextLine();

                    //if the date is out of format then throw an exception
                    if(line1.length() == 10){
                        addToMap.getMembers(line1);
                    }
                    else throw new InvalidParameterException("Invalid input. Recheck the format");

                    break;
                case 4:
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print the details of all the emails sent on the input date

                    String line3 = scanner.nextLine();

                    //if the date is out of format then throw an exception
                    if(line3.length() == 10) {
                        addToMap.getDetails(line3);
                    }
                    else throw new InvalidParameterException("Invalid input. Recheck the format");


                    break;
                case 5:
                    // code to print the number of recipient objects in the application
                    System.out.println(storage.getSize());
                    break;

            }
        }catch (InputMismatchException e){
            System.out.println("Invalid input type.Please enter a number between 1 and 5");
        }


        //serialize the maps
        SerializeDeserialize.Serialize(sentMails.getSentMap(), DescriptionMap.getMap());


    }
}


