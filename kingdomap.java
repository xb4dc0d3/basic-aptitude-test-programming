// to avoid stack overflow:
//   javac kingdommap.java
//   java -Xss4m kingdommap
// for linux user, to copy content of input.in, run:  xclip -sel clip < input.in

import java.util.Scanner;
import java.util.Arrays;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;

class kingdommap
{
	public static void main(String[] args)
	{
		// Untuk ngeprint output ke file output.txt
		/////////////////////////////////////////////////////////
        try {                                                  //
            File file = new File("output.txt");                //
            FileOutputStream fos = new FileOutputStream(file); //
            PrintStream ps = new PrintStream(fos);             //
            System.setOut(ps);                                 //
        } catch (FileNotFoundException e) {                    //
            e.printStackTrace();                               //
        }                                                      //

        Scanner sc = new Scanner(System.in);
        int banyakTestCase = sc.nextInt();

		// test case ke-i, baris ke-j, kolom ke-k
        int i, j, k;

		// Bikin array of Map
			// Analogi:  int[] arrayOfInteger = new int[banyakElemen]
        Map[] arrayOfMap = new Map[banyakTestCase];

        for (i = 0; i < banyakTestCase; i++)
		{
            int N = sc.nextInt();
            int M = sc.nextInt();

			// Kita sudah bikin arrayOfMap, tapi tiap elemennya belum diinstansiasi
				// elemen yang belum diinstansiasi akan bernilai null
				// Analogi:  arrayOfInteger[i] = value;
            arrayOfMap[i] = new Map();

            arrayOfMap[i].baris = new String[N];// Map ke-i memiliki N baris

            for (j = 0; j < N; j++)
			{
				// Nerima String baris ke-j untuk map testCase ke-j
                arrayOfMap[i].baris[j] = sc.next();
            }
        }

        for (i = 0; i < banyakTestCase; i++)
		{
            int contested = 0;// Jumlah region di map i yg punya 2 atau lebih huruf yg berbeda

			// List faction yg paling sedikit menguasai 1 region di map i
				// Jika suatu huruf ada n buah, berarti huruf tersebut menguasai n region
            String penguasaRegion = "";

            System.out.println("Case "+(i+1)+":");

			// Looping sebanyak baris Map testCase ke-i
            for (j = 0; j < arrayOfMap[i].baris.length; j++)
			{
				// Looping sebanyak jumlah karakter dari baris ke-j Map ke-i
                for (k = 0; k < arrayOfMap[i].baris[j].length(); k++)
				{
					// Kirim tim pencari ke semua titik di map (j,k)
						// Seandainya titik tersebut bukan '#',
						// maka itu adalah region baru yg belum di jelajahi
						// lihat method kirimTimPencari() untuk memahaminya
                    kirimTimPencari(arrayOfMap, i, j, k);

                    if(checkContested()){
                        contested++;
                    }else if(armiesInRegion.length() != 0){
						// Kalau tidak contested dan di region itu tidak kosong,
							// berarti cuma ada 1 faction, penguasa region tersebut.
						// armiesInRegion.length() != 0 bukan armiesInRegion.length() == 1
							// karena 1 faction bisa terdiri dari beberapa army.
                        penguasaRegion += armiesInRegion.charAt(0);
                    }

					// Reset daftar army, untuk diisi lagi oleh kirimTimPencari()
						// saat menemukan region baru
					// (sebenarnya ini agak sia2 karena armiesInRegion selalu
						// direset walau sudah kosong)
                    armiesInRegion = "";
                }
            }

			// Urutkan penguasaRegion berdasarkan abjad
            penguasaRegion = sortStringByChar(penguasaRegion);
            if(penguasaRegion.length() != 0){
				// Cetak daftar penguasa dan jumlah region yg mereka kuasai
                printArmies(penguasaRegion);
            }
            System.out.println("contested "+contested);
        }
    }

	// Adalah method untuk mengecek karakter pada suatu koordinat (j,k) di map ke-i.
		// Tidak hanya itu, setelah mengecek suatu koordinat, tim pencari akan berpencar
		// ke segala penjuru dan menandai daerah yg sudah dicek oleh mereka
		// sehingga daerah yg sudah dicek tidak perlu dicek ulang oleh tim pencari lain
		// Keterangan: j -> baris; k -> kolom.
    static void kirimTimPencari(Map[] arrayOfMap,int i, int j, int k)
	{
		// Kalau '#', itu berarti gunung atau jalan yg sudah ditempuh/dicek sebelumnya,
			// jidak perlu disusuri lagi.
        if(arrayOfMap[i].baris[j].charAt(k) != '#')
		{
			// Tapi kalau bukan,

            if(arrayOfMap[i].baris[j].charAt(k) != '.'){
				// Kalau bukan '.', pasti salah satu faction
				// Masukkan karakter faction tersebut ke dalam static String daftar tim pencari
                armiesInRegion += arrayOfMap[i].baris[j].charAt(k);
            }

			// Tandai koordinat ini sudah dicek dengan merubah karakter ke-k pada String baris ke-j
            arrayOfMap[i].baris[j] = ubahCharKe(arrayOfMap[i].baris[j], k);

			// Lanjutkan perjalanan, berpencar.

			// Selagi belum di perbatasan sebelah utara,
            if(j > 0){
				// Kirim tim pencari ke arah utara
                kirimTimPencari(arrayOfMap, i, j-1, k);
            }

			// Selagi belum di perbatasan sebelah barat,
            if(k > 0){
				// Kirim tim pencari ke arah barat
                kirimTimPencari(arrayOfMap, i, j, k-1);
            }

			// Selagi belum di perbatasan sebelah selatan,
            if(j < arrayOfMap[i].baris.length-1){
				// Kirim tim pencari ke arah selatan
                kirimTimPencari(arrayOfMap, i, j+1, k);
            }

			// Selagi belum di perbatasan sebelah timur,
            if(k < arrayOfMap[i].baris[j].length()-1){
				// Kirim tim pencari ke arah timur
                kirimTimPencari(arrayOfMap, i, j, k+1);
            }
        }
    }

	// Semua huruf yang ditemukan oleh tim pencari dimasukkan kesini
		// suatu huruf bisa muncul beberapa kali
		// berurutan dari yang pertama ketemu sampai yang terakhir
    static String armiesInRegion = "";

	// Cek apakah region di map punya 2 atau lebih huruf yg berbeda
    static boolean checkContested()
	{
		// Jika tidak ada huruf yang terdata di armiesInRegion
        if(armiesInRegion.length() == 0){
            return false;
        }

        char patokan = armiesInRegion.charAt(0);
        for (int i = 1; i < armiesInRegion.length(); i++)
		{
			// Jika huruf ke-2 dan seterusnya ada yg berbeda dari huruf pertama
            if(armiesInRegion.charAt(i) != patokan){
                return true;
            }
        }

		// Kalo sampe sini, berarti cuma ada 1 huruf
        return false;
    }


    static void printArmies(String penguasaRegion)
	{
		// Patokan adalah penguasa yang mewakili factionnya
			// Misal: penguasaRegion = "aabbbcccc", maka patokan
			// adalah char ke-0, setelah itu berubah jadi char ke-2,
			// lalu terakhir jadi char ke-5
        char patokan = penguasaRegion.charAt(0);

        int count = 0;// Jumlah region yg dikuasai faction si patokan

        for (int i = 0; i < penguasaRegion.length(); i++)
		{
            if(penguasaRegion.charAt(i) == patokan){
				// Kalau huruf ke-i masih sama dengan patokan,
					// tambah jumlah region yg dikuasai faction si patokan
                count++;
            }else{
				// Kalau sudah bukan faction yg sama dengan patokan,
					// print patokan dan jumlah region yg dikuasai
                System.out.println(patokan+" "+count);

				// Patokan sudah berganti, penguasa region yg sudah dihitung
					// baru 1 (patokan baru itu sendiri)
                patokan = penguasaRegion.charAt(i);
                count = 1;
            }

            if(i == penguasaRegion.length()-1){
				// Kalau sudah diujung daftar penguasaRegion,
					// print patokan terakhir dan jumlah region yg dikuasai
                System.out.println(patokan + " " + count);
            }
        }
    }

	// Urutkan String berdasarkan abjad
    static String sortStringByChar(String penguasaRegion)
	{
        char[] tempArray = penguasaRegion.toCharArray();
        Arrays.sort(tempArray);
        String sorted = new String(tempArray);
        return sorted;
    }

	// Ini adalah fungsi untuk merubah karakter ke-k dari sebuah String menjadi '#'
    static String ubahCharKe(String baris, int k)
	{
        String barisBaru = baris.substring(0, k) + '#' + baris.substring(k+1);
		// Read about substring:	https://www.javatpoint.com/substring
        return barisBaru;
    }


}

class Map {
    String[] baris;// Setiap Map punya array of baris, mulai dari baris[0] (baris pertama) dst.
}