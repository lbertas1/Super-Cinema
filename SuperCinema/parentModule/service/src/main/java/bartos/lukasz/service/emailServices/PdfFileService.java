package bartos.lukasz.service.emailServices;

import bartos.lukasz.dto.getModel.*;
import bartos.lukasz.enums.TicketType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PdfFileService {

    public static void createTicket(GetTicket ticketDto, GetUser userDto, List<GetSeat> seats, GetMovie movieDto, GetSeance seanceDto,
                                    GetCity cityDto, GetCinema cinemaDto, GetCinemaRoom cinemaRoomDto) {

        Rectangle page = new Rectangle(PageSize.A4);
        page.setBackgroundColor(new BaseColor(255, 30, 30));
        page.setBorder(Rectangle.BOX);
        page.setBorderWidth(20);
        page.setBorderColor(BaseColor.BLACK);
        Document ticketPdf = new Document(page);

        try {
            Files.deleteIfExists(Paths.get("Ticket.pdf"));
            PdfWriter.getInstance(ticketPdf, new FileOutputStream(new File("Ticket.pdf")));

            int ticketNumber = new Random().nextInt(1000) + 1000;
            boolean isPaid = ticketDto.getTicketType().equals(TicketType.O);

            ticketPdf.open();

            ticketPdf.add(setTitle(cinemaDto.getName()));

            ticketPdf.add(setTicketNumber(ticketNumber));

            ticketPdf.add(setMovieParagraph(movieDto));

            Font userDataFont = new Font();
            userDataFont.setSize(15);
            userDataFont.setStyle(Font.BOLD);
            String name = "Name: " + userDto.getName();
            String surname = "Surname: " + userDto.getSurname();
            String age = "Age: " + userDto.getAge();
            String email = "Email: " + userDto.getEmail();

            ticketPdf.add(setUserDataParagraphName(name, userDataFont));

            ticketPdf.add(setParagraphWithStandardSpace(surname, userDataFont));

            ticketPdf.add(setParagraphWithStandardSpace(age, userDataFont));

            ticketPdf.add(setParagraphWithBiggerSpace(email, userDataFont));

            ticketPdf.add(setParagraphWithStandardSpace("Price: " + ticketDto.getPrice().toString(), userDataFont));

            ticketPdf.add(setParagraphWithBiggerSpace("Is paid: " + isPaid, userDataFont));

            Font boldFont = new Font();
            boldFont.setStyle(Font.BOLD);

            ticketPdf.add(setTableHeader(boldFont));

            seats.forEach(getSeat -> {
                try {
                    ticketPdf.add(setTableContent(boldFont, cinemaRoomDto, getSeat, seanceDto));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });

            ticketPdf.add(addClause());

            ticketPdf.add(addRegards(boldFont));

            ticketPdf.add(addQrCode(userDto, cityDto, cinemaDto, cinemaRoomDto, movieDto, seats,
                    seanceDto, ticketDto, isPaid, ticketNumber));

            ticketPdf.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Paragraph setTitle(String cinemaName) {
        Font title = new Font();
        title.setSize(30);
        title.setStyle(Font.BOLDITALIC);
        title.setStyle(Font.UNDERLINE);
        Paragraph paragraph = new Paragraph(cinemaName, title);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    private static Paragraph setTicketNumber(int ticketNumber) {
        Font ticketNumberFont = new Font();
        ticketNumberFont.setStyle(Font.BOLD);
        ticketNumberFont.setStyle(Font.ITALIC);
        ticketNumberFont.setSize(15);
        String ticket = "Ticket number: " + ticketNumber;
        Paragraph ticketNumberParagraph = new Paragraph(ticket, ticketNumberFont);
        ticketNumberParagraph.setSpacingBefore(10);
        ticketNumberParagraph.setSpacingAfter(10);
        ticketNumberParagraph.setAlignment(Element.ALIGN_CENTER);
        return ticketNumberParagraph;
    }

    private static Paragraph setMovieParagraph(GetMovie movieDto) {
        Font movieFont = new Font();
        movieFont.setSize(15);
        movieFont.setStyle(Font.BOLD);
        String movie = "Movie: \"" + movieDto.getTitle().toUpperCase() + "\" Genre: " + movieDto.getFilmGenre().toUpperCase();
        Paragraph movieParagraph = new Paragraph(movie, movieFont);
        movieParagraph.setAlignment(Element.ALIGN_CENTER);
        movieParagraph.setSpacingBefore(5);
        movieParagraph.setSpacingAfter(5);
        return movieParagraph;
    }

    private static Paragraph setUserDataParagraphName(String name, Font userDataFont) {
        Paragraph userDataParagraphName = new Paragraph(name, userDataFont);
        userDataParagraphName.setSpacingBefore(10);
        userDataParagraphName.setSpacingAfter(5);
        return userDataParagraphName;
    }

    private static Paragraph setParagraphWithStandardSpace(String surname, Font userDataFont) {
        Paragraph userDataParagraphSurname = new Paragraph(surname, userDataFont);
        userDataParagraphSurname.setSpacingAfter(5);
        return userDataParagraphSurname;
    }

    private static Paragraph setParagraphWithBiggerSpace(String email, Font userDataFont) {
        Paragraph userDataParagraphEmail = new Paragraph(email, userDataFont);
        userDataParagraphEmail.setSpacingAfter(15);
        return userDataParagraphEmail;
    }

    private static PdfPTable setTableHeader(Font font) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(4);
        tableHeader.setWidthPercentage(100);
        tableHeader.setSpacingBefore(15);
        tableHeader.setSpacingAfter(0);

        float[] columnWidths = {1f, 1f, 1f, 1f};
        tableHeader.setWidths(columnWidths);

        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Room number", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Row", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Place", font))));
        tableHeader.addCell(setCellProperties(new PdfPCell(new Paragraph("Screening date", font))));

        return tableHeader;
    }

    private static PdfPTable setTableContent(Font font, GetCinemaRoom cinemaRoomDto, GetSeat seatDto, GetSeance seanceDto) throws DocumentException {
        PdfPTable tableValue = new PdfPTable(4);
        tableValue.setWidthPercentage(100);
        tableValue.setSpacingBefore(0);
        tableValue.setSpacingAfter(0);

        float[] valueColumnsWidths = {1f, 1f, 1f, 1f};
        tableValue.setWidths(valueColumnsWidths);

        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(cinemaRoomDto.getRoomNumber().toString(), font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(seatDto.getSeatRow().toString(), font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(seatDto.getPlace().toString(), font))));
        tableValue.addCell(setCellProperties(new PdfPCell(new Paragraph(seanceDto.getScreeningDate().toString(), font))));

        return tableValue;
    }

    private static PdfPCell setCellProperties(PdfPCell pCell) {
        pCell.setBorderColor(BaseColor.BLACK);
        pCell.setBorderWidth(3);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pCell.setUseBorderPadding(true);
        return pCell;
    }

    private static Paragraph addClause() {
        String someText = "This ticket in the civil law terms is a contract between the cinema and the customer. " +
                "Croquettes recipe. Boil and salt the mushrooms, fry the pancakes separately, " +
                "then wrap the mushrooms in the pancake and bake the croquettes in a pan in breadcrumbs and eggs. " +
                "You are not allowed to bring your croquettes to the cinema room." +
                "The ticket is personal and cannot be resold." +
                "How to pickle cucumbers. Put them in a jar, add what you need and start souring. " +
                "When ready, eat with gusto. Remember that pickled vegetables are high in vitamin C and other crap.";
        Paragraph text = new Paragraph(someText);
        text.setFirstLineIndent(25);
        text.setSpacingBefore(15);
        text.setSpacingAfter(25);
        return text;
    }

    private static Paragraph addRegards(Font font) {
        font.setSize(15);
        String regardsText = "We wish you a nice seance!";
        Paragraph regards = new Paragraph(regardsText, font);
        regards.setAlignment(Element.ALIGN_CENTER);
        regards.setSpacingAfter(25);
        return regards;
    }

    private static Image addQrCode(GetUser userDto, GetCity cityDto, GetCinema cinemaDto,
                                   GetCinemaRoom cinemaRoomDto, GetMovie movieDto, List<GetSeat> seatDto,
                                   GetSeance seanceDto, GetTicket ticketDto, boolean isPaid, int ticketNumber) throws BadElementException {

        AtomicInteger counter = new AtomicInteger(1);

        String seats = seatDto
                .stream()
                .map(getSeat -> "\n" + counter + ". row: " + getSeat.getSeatRow() + " place: " + getSeat.getPlace())
                .collect(Collectors.joining());

        String qrCodeMessage = "Name: " + userDto.getName() + " surname: " + userDto.getSurname() + " age: " + userDto.getAge() +
                " email: " + userDto.getEmail() + " city: " + cityDto.getName() + " cinema: " + cinemaDto.getName() +
                " movie: " + movieDto.getTitle() + " room: " + cinemaRoomDto.getRoomNumber() + " date: " + seanceDto.getScreeningDate() +
                seats + "\n" + " price: " + ticketDto.getPrice().toString() + " is paid: " + isPaid + " ticket number: " + ticketNumber;

        BarcodeQRCode my_code = new BarcodeQRCode(qrCodeMessage, 200, 200, null);
        Image qr_image = my_code.getImage();
        qr_image.setAlignment(Element.ALIGN_RIGHT);
        return qr_image;
    }
}
