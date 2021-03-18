import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class MethodS implements Function{


    private String link = "https://www.cbr-xml-daily.ru/daily_json.js";
    private URL url;
    private String allValue;
    private String[] valueArray;
    private List<String> array = new ArrayList<>();
    private final ZoneId timeZone = ZoneId.of("Europe/Moscow");
    private final LocalDateTime today = LocalDateTime.now(timeZone);
    private Map<String, Double> charCodAndValue = new HashMap<>();
    private Map<String, String> allInfoAboutCurrency = new LinkedHashMap<>();
    private List<String> arrayChosenCurrency = new ArrayList<>();
    private int message = 0;
    private int message2 = 0;

    {
try {
         url = new URL(link);
        Scanner scan = new Scanner(url.openStream());
        while (scan.hasNextLine()) {
            array.add(scan.nextLine());
        }
    } catch (IOException e) {
        System.out.println("Файл из сети не найден. Проверьте интернет соединение");
    }
    }

    String separator = File.separator;
    File saveFile = new File(separator + "C:" + separator + "Users" + separator + "wella" + separator + "Desktop"
            + separator + "CFT" + separator + "src" + separator + "inProgressChosenCurrency.txt");
    File saveFile2 = new File(separator + "C:" + separator + "Users" + separator + "wella" + separator + "Desktop"
            + separator + "CFT" + separator + "src" + separator + "ChosenCurrency.txt");

    @Override
    public void program(){
        getRefreshCurrency();
        Scanner scan = new Scanner(System.in);
        System.out.println();
        System.out.println("Для вывода полного списка валют, введите -> \"All\"");
        System.out.println("Для вывода избранного списка валют, введите -> \"Show\"");
        System.out.println("Для конвертации рублей в валюту, введите -> \"Conversion\"");
        System.out.println("Для создания нового списка валют, введите -> \"Create\"");
        System.out.println("Для завершения программы, введите -> \"Exit\"");
        String command = scan.nextLine();
        if (command.toLowerCase().equals("all")){
            getAllCurrency();
        } else if(command.toLowerCase().equals("show")){
            if (message2 < 1){
                System.out.println("При создании нового списка, данный список будет удален");
                message2++;
            }
            printChosenCurrency();
        } else if(command.toLowerCase().equals("conversion")){
            getConversionRubToCurrency();
        } else if (command.toLowerCase().equals("create")){
            if (message < 1){
                System.out.println("При перезапуске программы, будет создан новый избранный лист. " +
                        "\nВо время работы текущей программы, валюта будет добавляться и сохранится");
                message++;
            }
            NewChosenCurrency();
        } else if (command.toLowerCase().equals("exit")){
            System.out.println("Хорошего дня!");
            return;
        } else {
            System.out.println("Команда не распознана");
            program();
        }
    }

    @Override
    public void NewChosenCurrency(){
        try {
            PrintWriter pw = new PrintWriter(saveFile);
            Scanner scanner1 = new Scanner(System.in);
            PrintWriter pw2 = new PrintWriter(saveFile2);
            Scanner scanner2 = new Scanner(saveFile);

            System.out.println("Введите тикер валюты, которую хотите добавить в избранное");
            System.out.println("Для выхода, введите \"Exit\"");
            String chosenCurrency = " \""+scanner1.nextLine().toUpperCase()+"\"";
            if (chosenCurrency.toLowerCase().equals(" \"exit\"")){
                program();
            }
            pw.println(chosenCurrency.toUpperCase() + allInfoAboutCurrency.get(chosenCurrency.toUpperCase()));
            pw.close();

            while (scanner2.hasNextLine()) {
                String line = scanner2.nextLine();
                arrayChosenCurrency.add(line);
            }
            for (int i = 0; i < arrayChosenCurrency.size(); i++){
                pw2.println(arrayChosenCurrency.get(i));
            }
            pw2.close();
            program();

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void getConversionRubToCurrency(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите тикер валюты, в которую необходимо конвертировать");
            System.out.println("Для выхода, введите \"Exit\"");
            String currency = " \""+scanner.nextLine().toUpperCase()+"\"";

            if (currency.toLowerCase().equals(" \"exit\"")){
                program();
            }

            System.out.println("Введите сумму в рублях, которую необходимо конвертировать");
            int number = scanner.nextInt();

            String formattedDouble = new DecimalFormat("#0.00").format(charCodAndValue.get(currency.toUpperCase()) * number);
            System.out.println(formattedDouble + " " + currency);
        }
        catch (InputMismatchException | NullPointerException e){
            System.out.println("Сумму введите цифрами");
            getConversionRubToCurrency();
        }
        program();
    }

    @Override
    public void printChosenCurrency(){
        try {
            Scanner scanner = new Scanner(saveFile2);
            List<String> numbers = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                numbers.add(line);
            }
            for(int i = 0; i < numbers.size(); i++){
                System.out.println(numbers.get(i));
            }
            program();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void getAllCurrency() {
        int j = 15;
        int charCode = 9;
        int name = 11;
        int value = 12;
        int previous = 13;
        int count = 1;

        for (int i = 9; i <= array.size(); i++) {
            if (i == j) {
                allValue = array.get(name) + array.get(charCode) + array.get(value) + array.get(previous);
                valueArray = allValue.split("[:,]");

                System.out.println(count + ") " + valueArray[1] + " (" + valueArray[3] + ") курс : " + Double.parseDouble(valueArray[5]) +
                        " (предыдущий курс : " + Double.parseDouble(valueArray[7]) + ")");

                charCodAndValue.put(valueArray[3], Double.parseDouble(valueArray[5]));
                allInfoAboutCurrency.put(valueArray[3], valueArray[1] + " курс: " + valueArray[5] + " (предыдущий курс: " + valueArray[7] + ")");

                j += 9;
                charCode += 9;
                name += 9;
                value += 9;
                previous += 9;
                count++;
            }
        }
        program();
    }

    @Override
    public void addMap(){
        int j = 15;
        int charCode = 9;
        int name = 11;
        int value = 12;
        int previous = 13;

        for (int i = 9; i <= array.size(); i++) {
            if (i == j) {
                allValue = array.get(name) + array.get(charCode) + array.get(value) + array.get(previous);
                valueArray = allValue.split("[:,]");

                charCodAndValue.put(valueArray[3], Double.parseDouble(valueArray[5]));
                allInfoAboutCurrency.put(valueArray[3], valueArray[1] + " курс: " + valueArray[5] + " (предыдущий курс: " + valueArray[7] + ")");

                j += 9;
                charCode += 9;
                name += 9;
                value += 9;
                previous += 9;
            }
        }
    }

    @Override
    public void getRefreshCurrency(){
        try {
            if (today.getDayOfWeek().equals(DayOfWeek.MONDAY) && today.getHour()>= 10 && today.getHour()>= 30
                    || today.getDayOfWeek().equals(DayOfWeek.TUESDAY) && today.getHour()>= 15 && today.getHour()>= 30
                    || today.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) && today.getHour()>= 10 && today.getHour()>= 30
                    || today.getDayOfWeek().equals(DayOfWeek.THURSDAY) && today.getHour()>= 10 && today.getHour()>= 30
                    || today.getDayOfWeek().equals(DayOfWeek.FRIDAY) && today.getHour()>= 10 && today.getHour()>= 30){
                String newLink = "https://www.cbr-xml-daily.ru/daily_json.js";
                this.url = new URL(newLink);
            }
        }catch (MalformedURLException e){
            System.out.println("Файл из сети не найден. Проверьте интернет соединение");
        }
    }
}