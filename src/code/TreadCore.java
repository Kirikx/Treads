package code;

import java.io.*;

public class TreadCore {
    private volatile static char charn = 'A';
    public static DataOutputStream dos = null;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Задание 1");
        TreadCore monitor = new TreadCore();
        Thread t1 = new Thread(() -> {
            readChar(monitor, 'A', 'B');
        });
        Thread t2 = new Thread(() -> {
            readChar(monitor, 'B', 'C');
        });
        Thread t3 = new Thread(() -> {
            readChar(monitor, 'C', 'A');
        });
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("Конец задание 1");
        System.out.println(" ");

        System.out.println("Задание 2");
        try {
            dos = new DataOutputStream(new FileOutputStream("1.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Thread tWrite1 = new Thread(() -> {
            monitor.wriText(dos, "Поток записи 1 \n");
        });
        Thread tWrite2 = new Thread(() -> {
            monitor.wriText(dos, "Поток записи 2 \n");
        });
        Thread tWrite3 = new Thread(() -> {
            monitor.wriText(dos, "Поток записи 3 \n");
        });
        tWrite1.start();
        tWrite2.start();
        tWrite3.start();
        tWrite1.join();
        tWrite2.join();
        tWrite3.join();
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Конец задание 2");

        System.out.println("Задание 3");
        Mfy mfy = new Mfy();
        mfy.scan("doc1", 12);
        mfy.scan("doc2", 11);
        mfy.print("photo1", 11);
        mfy.print("photo2", 10);
    }

    private void wriText(DataOutputStream out, String text) {
        try {
            for (int i = 0; i < 10; i++) {
                out.writeUTF(text);
                System.out.print(text);
                Thread.sleep(20);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void readChar(Object monitor, char pre, char post) {

        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (charn != pre) {
                        monitor.wait();
                    }
                    System.out.print(charn);
                    charn = post;
                    monitor.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

class Mfy {
    private Object monitorPrint = new Object();
    private Object monitorScan = new Object();

    public void print(String text, int page) {
        new Thread(() -> {
            synchronized (monitorPrint) {
                for (int i = 1; i <= page; i++) {
                    System.out.println("Печать " + text + " страница-" + i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(" ");
                System.out.println(text + "-" + page + " стр. отпечатанно.");
            }
        }).start();

    }

    public void scan(String text, int page) {
        new Thread(() -> {
            synchronized (monitorScan) {
                for (int i = 1; i <= page; i++) {
                    System.out.println("Сканер " + text + " страница " + i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(" ");
                System.out.println(text + "-" + page + " стр. отсканированно.");
            }
        }).start();
    }
}
