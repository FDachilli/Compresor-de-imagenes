package Herramientas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Calculos {
	
    
static final int height = 395;
static final int width = 553;
static final double n = height*width;

//Se encarga de verificar el color de cada pixel, y guardar la cantidad total de repiticiones.
public static void verificar_color(int R, int G, int B, ArrayList<Color> vect){

int i = 0;
boolean find = false;
    
   while(i<=vect.size() && !find){
       if(i==vect.size()){
           Color nuevo_color = new Color();
           nuevo_color.setRGB(R, G, B);
           nuevo_color.addCantidad();
           vect.add(nuevo_color);
           find = true;
       }else
           if(vect.get(i).getR()==R && vect.get(i).getG()==G && vect.get(i).getB()==B){
               find = true;
               vect.get(i).addCantidad();
           }else
               i++;
   }
}

public static int verificar_indice (int R, int G, int B, ArrayList<Color> arreglo){
int i = 0;
    
   while(i<8){
           if(arreglo.get(i).getR()==R && arreglo.get(i).getG()==G && arreglo.get(i).getB()==B)
               return i;
           else
               i++;
   }
return -1;
}
                

public void calcular_probabilidad (ArrayList<Color> arreglo){
	
int R=0; 
int G=0; 
int B=0;


BufferedImage buffer_img = null;
try {
   buffer_img = ImageIO.read(new File("stars_8.bmp"));
} catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
}


/*Recorro la imagen y voy mirando cada pixel*/
   for(int i=0;i<buffer_img.getWidth();i++){ 
       for(int j=0;j<buffer_img.getHeight();j++){
    	   int rgb = buffer_img.getRGB(i, j);
    	   java.awt.Color color_aux = new java.awt.Color(rgb, true);
    	   
           R = color_aux.getRed(); 
           G = color_aux.getRed(); 
           B = color_aux.getRed(); 
           verificar_color(R,G,B,arreglo); 
           // Resultados de la forma[(R,G,B),(R,G,B),(155,155,2)]
       }//Fin del for para recorrer pixeles 
   }//Fin del for para recorrer pixeles
    
for(int i=0; i<8; i++){         
   arreglo.get(i).setProbabilidad((double) arreglo.get(i).getCantidad() / n);
}
}

public static void calcular_prob_condicional (ArrayList<Color> arreglo, double matriz [][]){
int R1=0;
int G1=0;
int B1=0;
int R2=0; 
int G2=0;
int B2=0;
int [] transiciones = new int [8]; //guardo la cantidad de transiciones para calcular la probabilidad
BufferedImage buffer_img = null;
try {
	buffer_img = ImageIO.read(new File("stars_8.bmp"));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

/*Recorro la imagen y voy mirando cada pixel*/ 
	for(int i=0;i<buffer_img.getWidth();i++){ 
		for(int j=0;j<buffer_img.getHeight();j+=2){
	    	int rgb = buffer_img.getRGB(i, j);
	    	java.awt.Color color_aux = new java.awt.Color(rgb, true);
			R1 = color_aux.getRed(); 
			G1 = color_aux.getRed(); 
			B1 = color_aux.getRed(); 
			if ((j==buffer_img.getHeight()-1) && (i!=buffer_img.getWidth())){
				//Convencion (si los pixeles son impar considero el ultimo como dos iguales)
				R2 = R1; 
				G2 = G1; 
				B2 = B1;
			} 
			else{
				buffer_img.getRGB(i, j+1);
		    	color_aux = new java.awt.Color(rgb, true);
				R2 = color_aux.getRed(); 
				G2 = color_aux.getRed(); 
				B2 = color_aux.getRed();
			}
					
			int indice1= verificar_indice(R1,G1,B1,arreglo);
			int indice2= verificar_indice(R2,G2,B2,arreglo);
			//busco indices para incrementar la matriz
			matriz[indice2][indice1]=matriz[indice2][indice1]+1;
			//incremento las transiciones del simbolo
			transiciones [indice1]= transiciones [indice1]+1;			
			
			// Resultados de la forma[(R,G,B),(R,G,B),(155,155,2)]
		}//Fin del for para recorrer pixeles 
	}//Fin del for para recorrer pixeles
	for (int k=0; k<8;k++)
		for(int h=0; h<8; h++){
			matriz [k][h]=matriz[k][h]/transiciones[k];
		}
}

public static void probabilidad_pares (ArrayList<Color> arreglo, double[][] matriz, ArrayList <Color> pares ){
for (int i=0; i<8; i++)
	for (int j=0; j<8; j++){
		Color aux= new Color();
		aux.setRGB(i, i, i);
		
		Color c= new Color();
		c.setRGB(j, j, j);
		aux.addColor(c);
		
		aux.setProbabilidad(arreglo.get(i).getProbabilidad() * matriz [i][j]);
		pares.add(aux);
	}

double suma=0;

for (int h=0; h<pares.size(); h++){
	suma=suma+pares.get(h).getProbabilidad();
}
}

public static double longitud_media (ArrayList <Color> colores){
double l=0;
for (int i=0; i< colores.size();i++){
	l=l+(colores.get(i).getProbabilidad()*colores.get(i).getLongitud());
}

if (colores.size()==8)
	return l;
else
	return l/2;
}

public static double entropia_sin_memoria (ArrayList <Color> colores){
double l=0;


for (int i=0; i< colores.size();i++){
	l=l+(colores.get(i).getProbabilidad()*(Math.log10(colores.get(i).getProbabilidad())/Math.log10(2)));
}

return -l;
}

public static double entropia_con_memoria (double [][] matriz, ArrayList <Color> colores){
double h = 0;
for (int i=0; i<8; i++)
	for (int j=0; j<8;j++){
		h=h+(colores.get(i).getProbabilidad()*(-matriz[i][j]*(Math.log10(matriz[i][j])/Math.log10(2))));
	}
return h;
}
}
