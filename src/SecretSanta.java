import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SecretSanta {

	Boolean[] santa;
	Boolean[] chosen;
	boolean santaCompleted = true;
	boolean chosenCompleted = true;

	public static void main(String[] args) {
	

		SecretSanta ss = new SecretSanta();
		ss.fileRead();
	}

	public void fileRead() {

		List<String> aList = new ArrayList<String>();

		int size = 0;

		try {

			File mf = new File("D:\\Cogni.txt");

			BufferedReader br = new BufferedReader(new FileReader(mf));

			String line = null;

			while ((line = br.readLine()) != null) {
				size = size + 1;
				aList.add(line);
			}

			br.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		chosen = new Boolean[size];
		santa = new Boolean[size];
		Arrays.fill(chosen, false);
		Arrays.fill(santa, false);

		Random rand = new Random();

		while (true) {
			int randomInt1 = rand.nextInt(size);
			int randomInt2 = rand.nextInt(size);

			String p1 = aList.get(randomInt1);
			String p2 = aList.get(randomInt2);

			if (p1 == p2) {
				// do nothing
			}
			if ((p1 != p2) && (santa[randomInt1] == false) && (chosen[randomInt2] == false)) {

				String[] person1 = p1.split(" ");
				String[] person2 = p2.split(" ");

				String message = person1[0] + ", you are " + person2[0] + " " + person2[1] + "'s secret Santa!";
				sendEmail(person1[2], message);

				santa[randomInt1] = true;
				chosen[randomInt2] = true;

				if (arrCompleted(santa) == true && arrCompleted(chosen) == true) {
					break;

				}
			}
		}
	}

	boolean arrCompleted(Boolean[] boolarray) {
		for (int i = 0; i < boolarray.length; i++) {
			if (boolarray[i] == false) {
				return false;
			}
		}
		return true;
	}

	public void sendEmail(String to, String mess) {

		try {

			String host = "smtp.gmail.com";
			String username = "nemosecretsanata@gmail.com";
			String password = "SecretSanta@NeMo";
			String email = "nemosecretsanata@gmail.com";
			Properties props = System.getProperties();

			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", username);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "25");
			props.put("mail.smtp.starttls.enable", "true");

			Session s = Session.getDefaultInstance(props, null);
			Message message = new MimeMessage(s);
			message.setFrom(new InternetAddress("dineshonjava@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Secret Santa");

			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "<img src=\"cid:image\">"
					+ "<H6>" + mess + "</H6> <H3>I hope you will bring something really nice to make up for the wait :P<H3>"
							+ "<H3>Be the Secret Santa on 24th Dec. <H3>"
							+ "<H1> Meri Christmas Ho! Ho! Ho.....!<H1>";
			messageBodyPart.setContent(htmlText, "text/html");
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource("D:\\Cogni.jpeg");
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<image>");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport t = s.getTransport("smtp");
			t.connect(host, 465, username, password);
			t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			t.close();
			System.out.println(mess + " Email sent to : "+to);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}