package code;


public class TestTread {
    static final int size = 10_000_000;
    static final int h = size / 2;

    public static void main(String[] args) {
        float[] arr = new float[size];
        float[] arr1 = new float[size];
        float[] arr2 = new float[size];

        arrayToOne(arr);
        long a = System.currentTimeMillis();

        System.out.println("Расчет в одном потоке");
        var1(arr);
        System.out.println("Время выполнения преобразования " + (System.currentTimeMillis() - a));
        System.arraycopy(arr, 0, arr1, 0, size); // Копируем массив для сравнения

        System.out.println();

        arrayToOne(arr);
        System.out.println("Расчет в двух потоках");
        a = System.currentTimeMillis();
        var2(arr);
        System.out.println("Время выполнения преобразования " + (System.currentTimeMillis() - a));
        System.arraycopy(arr, 0, arr2, 0, size); // Копируем массив для сравнения

        System.out.println("Проверка на равенство массивов: " + checkArray(arr1, arr2));

    }

    private static void arrayToOne(float arr[]) {
        for (int i = 0; i < arr.length; i++)
            arr[i] = 1;
    }

    private static void var1(float arr[]) {
//        for (int i = 0; i < arr.length; i++)
//            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        convert (arr, 0);
    }

    private static void convert(float arr[], int h) { // Применение конвертера со смещением позволяет добится идентичности результатов вычисления
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + (i + h) / 5) * Math.cos(0.2f + (i + h) / 5)
                    * Math.cos(0.4f + (i + h) / 2));
        }
    }
    private static void var2(float arr[]) {
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        System.arraycopy(arr, 0, a1, 0, h); // Разделяем
        System.arraycopy(arr, h, a2, 0, h);

        Thread t1 = new Thread(() -> TestTread.convert(a1, 0));
        Thread t2 = new Thread(() -> TestTread.convert(a2, h));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        System.arraycopy(a1, 0, arr, 0, h); // Склеиваем
        System.arraycopy(a2, 0, arr, h, h);
    }

    private static boolean checkArray(float arr1[], float arr2[]) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i])
                return false;
        }
        return true;
    }

}

