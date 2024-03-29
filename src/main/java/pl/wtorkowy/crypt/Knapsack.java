package pl.wtorkowy.crypt;

import pl.wtorkowy.cast.ToTab;

import java.util.Arrays;

public class Knapsack {
    private int[] privateKey;
    private int[] publicKey;
    private int[] cipherText;
    private byte[] decipherText;

    private int n, m, reverseN;
    private boolean superIncreasing;

    public Knapsack(int[] privateKey, int n, int m) {
        this.privateKey = privateKey;
        publicKey = new int[privateKey.length];
        this.n = n;
        this.m = m;
        reverseN = reverseN(n, m);
        generatePublicKey();
        if(checkSuperIncreasing() && Euklides.isRelativelyPrime(m, n))
            superIncreasing = true;
    }

    public void encrypt(byte[] text) {
        if(superIncreasing) {
            int tmp = 0;
            cipherText = new int[text.length/publicKey.length];
            for(int i = 0; i < text.length/publicKey.length; i++) {
                for (int j = 0; j < publicKey.length; j++) {
                    if(text[i*publicKey.length + j] == 1)
                        tmp += publicKey[j];
                }
                cipherText[i] = tmp;
                tmp = 0;
            }
        }
        else {
            cipherText = new int[] {0};
        }
    }

    public void decrypt(int[] text) {
        int tmp = 0;
        int length = privateKey.length;
        byte[] tmpByte = new byte[length];
        decipherText = new byte[length*text.length];
        for(int i = 0; i < text.length; i++) {
            tmp = (text[i]*reverseN)%m;
            for (int j = length - 1; j >= 0; j--) {
                if(privateKey[j] > tmp) {
                    tmpByte[j] = 0;
                }
                else {
                    tmp -= privateKey[j];
                    tmpByte[j] = 1;
                }
            }
            System.arraycopy(tmpByte, 0, decipherText, i * length, length);
        }
    }

    public int reverseN(int n, int m) {
        for(int i = 0; i < m; i++) {
            if (((n*i)%m) == 1) return (i);
        }
        return 0;
    }

    private void generatePublicKey() {
        for (int i = 0; i < privateKey.length; i++) {
            publicKey[i] = (privateKey[i] * n) % m;
        }
    }

    public boolean checkSuperIncreasing() {
        int tmp = 0;
        for (int value : privateKey) {
            if (value > tmp)
                tmp += value;
            else
                return false;
        }
        return true;
    }

    public boolean isSuperIncreasing() {
        return superIncreasing;
    }

    public byte[] getDecipherText() {
        return decipherText;
    }

    public String getDecipherTextString() {
        return new String(ToTab.toCharTab(ToTab.toIntTab(decipherText)));
    }

    public int[] getCipherText() {
        return cipherText;
    }

    public String getCipherTextString() {
        return Arrays.toString(cipherText);
    }

    public String getPublicKey() {
        return Arrays.toString(publicKey);
    }

    public int getCipherTextInt() {
        return cipherText[0];
    }
}
