import java.io.*;
import java.util.*;

public class Generate {

   static final String paramFileName = "parameters.txt";
   static final String propFileName = "stresstest.properties";

   // Anmerkung: Dieser Labyrinthgenerator ist prozedural programmiert
   // und aeusserst unzureichend kommentiert und dokumentiert. Er soll
   // keine Anregungen fuer die Umsetzung des Projekts bieten, sondern
   // dient ausschliesslich der Labyrintherzeugung.

   static boolean printField = false; // any command line argument sets this to true
   static double density = 3.0;

   static final char idWall = '0';
   static final char idIn = '1';
   static final char idOut = '2';
   static final char idStaticTrap = '3';
   static final char idDynamicTrap = '4';
   static final char idKey = '5';

   static final char wall = '+';
   static final char free = ' ';
   static final char cIn = '\u2639';
   static final char cOut = '\u263A';
   static final char rP = free;
   static final Random random = new Random();
   static final Properties param = new Properties();
   static final Properties prop = new Properties();

   static int[][] points;
   static int width, height, nrIn, nrOut, nrKeys, nrStaticTraps, nrDynamicTraps;
   static char[][] field;
   static boolean[][] occupied;

   static int [] nxt(int x, int y){
      boolean bad = field[x][y]!=wall;
      if (x==0) {
         if (y==1) return bad?nxt(1,0): new int[]{1,0};
         return bad?nxt(x,y-1): new int[]{x,y-1};
      } else if (x==height-1) {
         if (y==width-2) return bad?nxt(height-2,width-1): new int[]{height-2,width-1};
         return bad?nxt(x,y+1): new int[]{x,y+1};
      } else if (y==0) {
         if (x==height-2) return bad?nxt(height-1,1): new int[]{height-1,1};
         return bad?nxt(x+1,y): new int[]{x+1,y};
      } else {
         if (x==1) return bad?nxt(0,width-2): new int[]{0,width-2};
         return bad?nxt(x-1,y): new int[]{x-1,y};
      }
   }

   static int[] advance (int x, int y, int num){
      for (int i=0;i<num;i++) {
         int[]xy=nxt(x,y);x=xy[0];y=xy[1];
      }
      for (int i=0;i<2*(height+width);i++) {
         if (field[x][y]==wall) return new int[]{x,y};
         int[]xy=nxt(x,y);x=xy[0];y=xy[1];
         xy=nxt(x,y);x=xy[0];y=xy[1];
      }
      return new int[]{-1,-1};
   }

   static void markIO(int x, int y, char c){
      field[x][y]=c;
      if (x==0) {
         field[1][y]=free;
      } else if (x==height-1) {
         field[height-2][y]=free;
      } else if (y==0) {
         field[x][1]=free;
      } else {
         field[x][width-2]=free;
      }
   }

   static long init() throws Exception{
      long seed = read();
      random.setSeed(seed);
      field = new char[height][width];
      occupied = new boolean[height][width];
      for (int x = height - 1; x > -1; x--){
         for (int y = 0; y < width; y++){
            field[x][y]=wall;
            occupied[x][y]=false;
      }  }
      int idx = 1, nrRand = (int) ((3.0 / density) * (6 + Math.abs(random.nextInt())%((2*width+2*height)+1)));
      points = new int[nrIn+nrOut+nrStaticTraps+nrDynamicTraps+nrKeys+nrRand][2];
      if (points.length >= (height-2)*(width-3)) {
         System.out.println("Field too small. Choose larger one!");
         throw new Exception("field too small");
      }
      // In+Out
      points[0]=new int[]{modnar(1,height-2),0};
      markIO(points[0][0],points[0][1],'\u2639');
      for (int i = 1; i < nrIn+nrOut; i++, idx++) {
         points[idx]=advance(points[idx-1][0],points[idx-1][1],modnar(1,width+height));
         markIO(points[idx][0],points[idx][1],i<nrIn?'\u2639':cOut);
      }
      // Keys, Traps, random points
      while (idx < points.length) {
         do {points[idx]=new int[]{modnar(1,height-2),modnar(1,width-2)};}
         while (field[points[idx][0]][points[idx][1]]!=wall);
         if (idx<nrIn+nrOut+nrKeys)
            field[points[idx][0]][points[idx][1]]=idKey;
         else if (idx<nrIn+nrOut+nrKeys+nrStaticTraps)
            field[points[idx][0]][points[idx][1]]=idStaticTrap;
         else if (idx<nrIn+nrOut+nrKeys+nrStaticTraps+nrDynamicTraps)
            field[points[idx][0]][points[idx][1]]=idDynamicTrap;
         else
            field[points[idx][0]][points[idx][1]]=rP;
         idx++;
      }
      occupied[points[points.length-1][0]][points[points.length-1][1]]=true;
      return seed;
   }

   static void putField(){
      for (int x = height - 1; x > -1; x--){
         for (int y = 0; y < width; y++) {
            System.out.print(field[x][y]);
         }
         System.out.println();
   }  }

   static int modnar(int low, int high){
      if (low>=high) return low; return Math.abs(random.nextInt()) % (1+high-low) + low;
   }
   static boolean chance(int percentage){return Math.abs(random.nextInt())%100<percentage;}

   // return a nearest occupied point
   static int[] connectionPoint(int x, int y){
      for (int dist = 1; dist<10000; dist++){
         for (int xx = x-dist<1?1:x-dist; xx < height-1 && xx-x<dist+1; xx++){
            if (y+dist < width && occupied[xx][y+dist]) {int[]res={xx,y+dist}; return res;}
            if (y-dist > 0     && occupied[xx][y-dist]) {int[]res={xx,y-dist}; return res;}
         }
         for (int yy = y-dist<1?1:y-dist; yy < width-1 && yy-y<dist+1; yy++){
            if (x+dist < height && occupied[x+dist][yy]) {int[]res={x+dist,yy}; return res;}
            if (x-dist > 0      && occupied[x-dist][yy]) {int[]res={x-dist,yy}; return res;}
         }
      }
      return new int[]{x,y};
   }

   static void dirx(int y, int x1, int x2){
      if (x1==x2) return;
      if (field[x1][y]==wall) field[x1][y]=free;
      if (!occupied[x1][y]) occupied[x1][y]=true;
      dirx(y, x1+1,x2);
   }

   static void diry(int x, int y1, int y2){
      if (y1==y2) return;
      if (field[x][y1]==wall) field[x][y1]=free;
      if (!occupied[x][y1]) occupied[x][y1]=true;
      diry(x, y1+1,y2);
   }

   static void connect(int x1, int y1, int x2, int y2){
      int choice=0,minX=Math.min(x1,x2),maxX=Math.max(x1,x2),minY=Math.min(y1,y2),maxY=Math.max(y1,y2);
      if (y1==y2) {dirx(y1,minX,maxX);return;}
      if (x1==x2) {diry(x1,minY,maxY);return;}
      if (field[x1][y1]==idKey){
         //if (field[x1][y1]==rP) field[x1][y1]=free;
         choice=chance(50)?1:2;
      }
      if (choice == 1) {
         if (chance(50)) {
            dirx(minY,minX,maxX);
            if (x1<x2==y1<y2) {
               diry(maxX,minY,maxY);
               if (field[maxX][minY]==wall)field[maxX][minY]=free;
            } else {
               diry(minX,minY,maxY);
               if (field[minX][minY]==wall)field[minX][minY]=free;
            }
         } else {
            dirx(maxY,minX,maxX);
            if (x1<x2==y1<y2) {
               diry(minX,minY,maxY);
               if (field[minX][maxY]==wall)field[minX][maxY]=free;
            } else {
               diry(maxX,minY,maxY);
               if (field[maxX][maxY]==wall)field[maxX][maxY]=free;
            }
         }
      } else if (choice == 2 || field[x1][y1]==idStaticTrap || field[x1][y1]==idDynamicTrap || field[x1][y1]==free) {
         dirx(minY,minX,maxX);
         dirx(maxY,minX,maxX);
         diry(minX,minY,maxY);
         diry(maxX,minY,maxY);
         if (field[x1][y2]==wall)field[x1][y2]=free;
         if (field[x2][y1]==wall)field[x2][y1]=free;
      }
   }

   static void compute(long seed){
      int idx=modnar(1,points.length-1);
      boolean[] used=new boolean[points.length];
      for (int i=0; i<used.length; i++) used[i]=false;
      for (int i = 0; i < points.length; i++){
         idx = (idx+modnar(1,points.length-1))%points.length;
         while (used[idx])idx=(idx+1)%points.length;
         used[idx]=true;
         int x = Math.min(height-2,Math.max(1,points[idx][0])),
             y = Math.min(width-2,Math.max(1,points[idx][1]));
         int[]cp=connectionPoint(x,y);
         //System.out.println("connecting: ("+x+","+y+") to ("+cp[0]+","+cp[1]+")");
         connect(x,y,cp[0],cp[1]);
      }
      if (printField) putField();
   }

   static long read(){
      try {
         FileInputStream fileInput = new FileInputStream(new File(paramFileName));
         param.load(fileInput);
         fileInput.close();
      } catch (Exception e) {e.printStackTrace();}
      width = Integer.valueOf(param.getProperty("Width"));
      height = Integer.valueOf(param.getProperty("Height"));
      nrIn = Integer.valueOf(param.getProperty("NrIn"));
      nrOut = Integer.valueOf(param.getProperty("NrOut"));
      nrKeys = Integer.valueOf(param.getProperty("Keys"));
      nrStaticTraps = Integer.valueOf(param.getProperty("StaticTraps"));
      nrDynamicTraps = Integer.valueOf(param.getProperty("DynamicTraps"));
      String s = param.getProperty("Density");
      if (s!=null) density = Double.valueOf(s).doubleValue();
      s = param.getProperty("Seed");
      return s==null ? random.nextLong() : Long.valueOf(s).longValue();
   }

   static void write(long seed){
      //prop.setProperty("SeedValue", (Long.valueOf(seed)).toString());
      prop.setProperty("Width", ""+width);
      prop.setProperty("Height", ""+height);
      for (int x = height - 1; x > -1; x--){
         for (int y = 0; y < width; y++){
            String coordinates = y+","+(height-(x+1));
            char next = field[x][y];
            switch (next){
               case wall: prop.setProperty(coordinates, ""+idWall); break;
               case cIn: prop.setProperty(coordinates, ""+idIn); break;
               case cOut: prop.setProperty(coordinates, ""+idOut); break;
               default: break;
            }
            if (next >= '1' && next <= '9') prop.setProperty(coordinates, ""+next);
         }
      }
      try {
         FileOutputStream outFile = new FileOutputStream(new File(propFileName));
         prop.store(outFile,"Properties (Seed=" + seed + ")");
         outFile.close();
      } catch (Exception e) {e.printStackTrace();}
   }

   public static void main(String args[]) {
      printField=(args!=null && args.length!=0);
      try {
         final long seed = init();
         compute(seed);
         write(seed);
      } catch (Exception e) {e.printStackTrace();}
   }
}