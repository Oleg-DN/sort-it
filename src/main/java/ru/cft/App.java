package ru.cft;

import java.io.*;

/**
 * Программа сортироровки слиянием нескольких файлов
 * Автор: Олег Найденко
 */
public class App {
    public static void main(String[] args) throws IOException {
        String sortMode = "-a";
        String dataType = null;
        int nFile = 0;

        for (int i = 0; i < args.length; i++) {
            //определяем режим сортировки
            if (args[i].equals("-a") || args[i].equals("-d")) {
                sortMode = args[i];
            }
            //определяем режим сортировки
            if (args[i].equals("-s") || args[i].equals("-i")) {
                dataType = args[i];
            }
            //считаем общее количество текстовых файлов
            if (args[i].indexOf(".txt") > 0) {
                nFile = nFile + 1;
            }
        }

        if (nFile < 2) {
            System.out.println("Не указаны файлы для сортировки");
            System.exit(1);
        }
        if (dataType == null) {
            System.out.println("Не указан тип данных!");
            System.exit(1);
        }

        String[][] arrData = new String[nFile-1][3]; //массив с одиночными данными из файлов
        String pathOut = null;

        nFile = 0;
        for (int i = 0; i < args.length; i++) {
            System.out.println(sortMode + "; " + dataType + "; " + pathOut + "; " + args[i]);
            if (args[i].indexOf(".txt") > 0) {
                if (pathOut == null) {
                    pathOut = args[i];
                } else {
                    arrData[nFile][0] = args[i];
                    arrData[nFile][1] = "0";
                    arrData[nFile][2] = "";
                    System.out.println(nFile + "; " + arrData[nFile][0]);
                    nFile = nFile +1;
                }
            }
        }

        boolean EndFile = false;    //переменная для определения отсутсвия строк с данными
        int nStr = 0;               //переменная для запоминания выгружаемой строки

        try {
            while (!EndFile) {
                //проверка данных на корректность
                for (int k = 0; k < arrData.length; k++) {
                    while (arrData[k][0] != null) {
                        //исключение законченных массивов
                        if (arrData[k][2] == null) {
                            arrData[k][0] = null;
                            arrData[k][1] = null;
                            break;
                        }
                        //исключение не числовых элементов при числовой сортировке
                        if (dataType.equals("-i") && !arrData[k][2].matches("-?\\d+(\\.\\d+)?")) {
                            arrData[k][2] = "";
                        }
                        //исключение элементов содержащих пробелы
                        if (arrData[k][2].indexOf(" ") > 0) {
                            arrData[k][2] = "";
                        }
                        //загрузка следующего элемента при пустой строке, иначе выход из цикла
                        if (arrData[k][2] == null || arrData[k][2].equals("")) {
                            arrData[k][1] = String.valueOf(Integer.parseInt(arrData[k][1]) + 1);
                            arrData[k][2] = ReadFile(arrData[k][0], Integer.parseInt(arrData[k][1]));
                        } else {
                            break;
                        }
                    }
                }
                //выбор строки по умолчанию для сравнения
                for (int k = 0; k < arrData.length; k++) {
                    if (arrData[k][2] != null) {
                        nStr = k;
                        break;
                    }
                }
                //поиск нужного значения согласно параметрам сортировки
                for (int k = 0; k < arrData.length; k++) {
//                    System.out.println(k + "; " + arrData[k][0] + "; " + arrData[k][1] + "; " + arrData[k][2]);
                    if (arrData[k][2] != null && !arrData[nStr][2].equals("") && !arrData[k][2].equals("")) {
                        if (dataType.equals("-i") && sortMode.equals("-a") && (Integer.parseInt(arrData[nStr][2]) > Integer.parseInt(arrData[k][2]))) {
                            nStr = k;
                        }
                        if (dataType.equals("-i") && sortMode.equals("-d") && (Integer.parseInt(arrData[nStr][2]) < Integer.parseInt(arrData[k][2]))) {
                            nStr = k;
                        }
                        if (dataType.equals("-s")) {
                            int c = arrData[nStr][2].compareTo(arrData[k][2]);
                            if (sortMode.equals("-a") && c > 0) {
                                nStr = k;
                            }
                            if (sortMode.equals("-d") && c < 0) {
                                nStr = k;
                            }
                        }
                    }
                }
                //сохранение значения
                if (arrData[nStr][2] != null) {
                    WriteFile(pathOut, arrData[nStr][2]);  //запись значения в файл
                    arrData[nStr][2] = "";                 //очистка переданного значения
                }

                EndFile = true;
                for (int k = 0; k < arrData.length; k++) {
                    if (arrData[k][2] != null) {
                        EndFile = false;
                        break;
                    }
                }
            }

        } catch (IOException a) {
            System.out.println(a.getMessage());
        }
    }

    static String ReadFile(String pathFile, int nLine) throws IOException {
        BufferedReader obj = new BufferedReader(new FileReader(pathFile));

        String val;
        int i = 1;
        while ((val = obj.readLine()) != null) {
            if (i == nLine) {
                break;
            }
            i++;
        }

        obj.close();
        return val;
    }

    static void WriteFile(String pathFile, String text) throws IOException {
        BufferedWriter obj = new BufferedWriter(new FileWriter(pathFile, true));
        String lineSeparator = System.getProperty("line.separator");

        obj.write(text + lineSeparator);
        obj.close();
    }
}
