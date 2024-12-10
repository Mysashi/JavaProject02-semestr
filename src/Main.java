import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        DataBase dataBase = new DataBase();
        Parser parser = new Parser();
//        dataBase.createSports(); // создать таблицу
//        dataBase.loadInData(parser.parseCsv()); // загрузка данных в базу данных
        dataBase.selectAvgCount(); // среднее значение
        dataBase.sortingTop(); // топ 3
        dataBase.createHistoPanel(); // гистограмма

    }
}