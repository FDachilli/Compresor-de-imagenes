package Herramientas;
public class Color implements Comparable<Color>{
	int R,G,B;
	int cantidad;
	double probabilidad;
	Color siguienteColor;
	String codigo;
	int longitud;
	
	public Color(){siguienteColor = null;};
	
	public int getCantidad()
			{return cantidad;}
	
	public void addColor(Color c){
		this.siguienteColor = c;
	}
	public double getProbabilidad(){
		return probabilidad;
	};
	
	public Color getSigColor(){
		return this.siguienteColor;
	}
	
	public Boolean tieneSig(){
		if(this.siguienteColor==null)
			return true;
		else
			return false;
	}
	public void addCantidad()
			{this.cantidad++;}

	public void setRGB(int R, int G, int B){
			 this.R = R; 
			 this.G = G; 
			 this.B = B;
	}
	
	public void setProbabilidad(double probabilidad){
		this.probabilidad = probabilidad;
	}
	
	public void setCodigo(String codigo){
		this.codigo=codigo;
	}
	
	public String getCodigo(){
		return this.codigo;
	}
	
	public void setLongitud(int longitud){
		this.longitud=longitud;
	}
	
	public int getLongitud(){
		return this.longitud;
	}
	public int getR(){
		return R;
	}

	public int getG(){
		return G;
	}

	public int getB(){
		return B;
	}


	public int compareTo(Color c2){
		if(this.probabilidad>c2.probabilidad)
			return 1;
		else
			if(this.probabilidad==c2.probabilidad)
				return 0;
			else
				return -1;
	}

}