package chkyrslf;

import java.awt.Color;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Create a pdf of some checks for yourself
 */
public class ChkYrSlf {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream("chkyrslf.properties");

		props.load(fis);

		String line1 = props.getProperty("line1");
		String line2 = props.getProperty("line2");
		String line3 = props.getProperty("line3");
		String line4 = props.getProperty("line4");

		String bankLine1 = props.getProperty("bankLine1");
		String bankLine2 = props.getProperty("bankLine2");
		String bankLine3 = props.getProperty("bankLine3");
		String bankLine4 = props.getProperty("bankLine4");

		String routingNumber = props.getProperty("routingNumber");
		String accountNumber = props.getProperty("accountNumber");

		String fractionalRoutingLine1 = props.getProperty("fractionalRoutingLine1");
//		String fractionalRoutingLine2 = "";

		int checkNumber = 1000;

		String file = "checks.pdf";
		PDDocument document = null;
		try {
			document = new PDDocument();

			PDPage page = new PDPage();
			document.addPage(page);

			PDFont boldFont = PDType1Font.HELVETICA_BOLD;
			PDFont normalFont = PDType1Font.HELVETICA;
			PDFont micrFont = PDTrueTypeFont.loadTTF(document, "GnuMICR-0.30/GnuMICR.ttf");

			PDPageContentStream content = new PDPageContentStream(document, page);

			for (float y = 0; y < 792; y += 198) {
				draw(content, boldFont, normalFont, micrFont,
						line1, line2, line3, line4,
						bankLine1, bankLine2, bankLine3, bankLine4,
						routingNumber, accountNumber,
						fractionalRoutingLine1, checkNumber++, y);
			}

			content.close();

			// save the document to the file stream.
			document.save(file);
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}

	private static void draw(PDPageContentStream content, PDFont boldFont, PDFont normalFont, PDFont micrFont,
			String line1, String line2, String line3, String line4,
			String bankLine1, String bankLine2, String bankLine3, String bankLine4,
			String routingNumber, String accountNumber, String fractionalRoutingNumber, int checkNumber, float y)
			throws Exception {

		// amount box
		content.setStrokingColor(Color.LIGHT_GRAY);
		content.drawPolygon(new float[] { 345F, 345F, 415F, 415F }, new float[] { 115 + y, 130 + y, 130 + y, 115 + y });

		content.setLineWidth(1F);
		content.drawLine(0, 1 + y, 430, 1 + y);		// horizontal divider
		content.drawLine(430, 1 + y, 430, 200 + y);	// vertical divider

		content.setStrokingColor(Color.BLACK);
		content.setNonStrokingColor(Color.BLACK);
		content.drawLine(55, 115 + y, 325, 115 + y);	// payee line
		content.drawLine(325, 115 + y, 325, 125 + y);
		content.drawLine(22, 90 + y, 370, 90 + y);		// dollars line
		content.drawLine(240, 145 + y, 350, 145 + y);	// date line
		content.drawLine(22, 38 + y, 200, 38 + y);		// memo line
		content.drawLine(220, 38 + y, 415, 38 + y);		// signature line

		content.beginText();
		content.setFont(boldFont, 10);

		content.moveTextPositionByAmount(21F, 170F + y);
		content.drawString(line1);

		content.setFont(normalFont, 10);
		content.moveTextPositionByAmount(0F, -11F);
		content.drawString(line2);

		content.moveTextPositionByAmount(0F, -11F);
		content.drawString(line3);

		content.moveTextPositionByAmount(0F, -11F);
		content.drawString(line4);

		content.setFont(normalFont, 8);
		content.moveTextPositionByAmount(0F, -11F);
		content.drawString("Pay to the");

		content.moveTextPositionByAmount(0F, -10F);
		content.drawString("Order of");

		content.setFont(boldFont, 12);
		content.moveTextPositionByAmount(310F, 3F);
		content.drawString("$");

		content.setFont(normalFont, 8);
		content.moveTextPositionByAmount(15F, -25F);
		content.drawString("Dollars");

		content.moveTextPositionByAmount(-105F, 55F);
		content.drawString("Date");

		content.moveTextPositionByAmount(-218F, -107F);
		content.drawString("For");

		content.setFont(normalFont, 7f);
		content.moveTextPositionByAmount(0F, 35F);
		content.drawString(bankLine1);
		content.moveTextPositionByAmount(0F, -8F);
		content.drawString(bankLine2);
		content.moveTextPositionByAmount(0F, -8F);
		content.drawString(bankLine3);
		content.moveTextPositionByAmount(0F, -8F);
		content.drawString(bankLine4);

		content.setFont(boldFont, 12);
		content.moveTextPositionByAmount(365F, 115F);
		content.drawString(Integer.toString(checkNumber));

		content.setFont(normalFont, 8);
		content.moveTextPositionByAmount(-15F, -20F);
		content.drawString(fractionalRoutingNumber);

		content.endText();

		content.beginText();
		content.setFont(micrFont, 12);
		content.moveTextPositionByAmount(23F, 18F + y);
		content.drawString("A" + routingNumber + "A " + accountNumber + "C " + checkNumber);
		content.endText();
	}
}
