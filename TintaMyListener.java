// Generated from Tinta.g4 by ANTLR 4.6
import java.util.Iterator;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import java.io.*;
import java.util.*;
import java.lang.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.stringtemplate.v4.*;
import org.stringtemplate.v4.ST.AttributeList;

/* Setting class
		setting that an object can have g = grossura c = cor */
class Settings {
	protected String g, c;

	public Settings ( String g, String c)
	{
		this.g = g;
		this.c = c;
	}
}

/* Point class
		point with x, y coordinates */
class Ponto{
   protected String x, y;

   public Ponto( String x, String y )
   {
   		this.x = x;
    	this.y = y;
   }
}

/**
 * This class provides an empty implementation of {@link TintaListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class TintaMyListener extends TintaBaseListener {

	protected STGroup group;
	protected ST code;
	protected int varCount = 0;
	protected String backGroundColor = "(255,255,255)";
	protected File pyFile;
	protected PrintWriter pyFile_writer;
 	protected ParseTreeProperty<String> objectNode = new ParseTreeProperty<>();

	protected HashMap<String, Ponto> pontos = new HashMap<String, Ponto>();
  	protected HashMap<String, ParseTreeProperty<String> > classes = new HashMap<>();
	protected HashMap<String, ST> actions = new HashMap<String, ST>();
	protected ArrayList<String> points = new ArrayList<>();

	// Gets Point (X,Y) by variable or by parsing ctx
	public Ponto getPonto(ParserRuleContext ctx , int childIndex) {
		// Checking if first point is a variable
 		if(ctx.getChild(childIndex).getChildCount() == 5){
 			String x = ctx.getChild(childIndex).getChild(1).getText();
 			String y = ctx.getChild(childIndex).getChild(3).getText();
	 		return new Ponto(x, y);
	 	}
	 	else{
	 		String name = ctx.getChild(childIndex).getText();
	 		if(pontos.containsKey(name))
	 		{
	 			return pontos.get(name);
	 		}
	 		else
	 		{
	 			errorPrint(2, name);
	 			return null;
	 		}
	 	}
	}

	public Settings getSettings(ParserRuleContext ctx , int childIndex) {
		String g = "int(width * height * 0.000005)";
		String c = "(0,0,0)";
		if(ctx.getChildCount() > childIndex)
 		{
 			String firstSetting = ctx.getChild(childIndex).getChild(0).getChild(0).getText();
 			if(ctx.getChildCount() > childIndex + 2){
 				String secondSetting = ctx.getChild(childIndex + 2).getChild(0).getChild(0).getText();
 				if(firstSetting.equals(secondSetting)){ errorPrint(9, firstSetting); }
 				else
 				{
 					// Getting Second Setting
		 			switch(firstSetting)
		 			{
		 				case "(C=":  c = ctx.getChild(childIndex + 2).getChild(0).getChild(1).getText(); break;
		 				case "(G=":  g = ctx.getChild(childIndex + 2).getChild(0).getChild(1).getText(); break;
		 				default : errorPrint(3, ctx.getParent().getChild(0).getText()); break;
		 			}
 				}
 			}
 			// Getting First Setting
 			switch(firstSetting)
 			{
 				case "(C=":  c = ctx.getChild(childIndex).getChild(0).getChild(1).getText(); break;
 				case "(G=":  g = ctx.getChild(childIndex).getChild(0).getChild(1).getText(); break;
 				default : errorPrint(3, ctx.getParent().getChild(0).getText()); break;
 			}
 		}

 		return new Settings(g, c);
	}

	public String finalCode() {
	  return code.render();
   	}

	@Override public void enterInputDescription(TintaParser.InputDescriptionContext ctx) {
		group = new STGroupFile("allTemplates.stg");
		code = group.getInstanceOf("code");
		code.add("name", "\'\'");
	}

 	@Override public void exitInputDescription(TintaParser.InputDescriptionContext ctx) {
		pyFile = new File("Result_Image.py");
		try{
			pyFile_writer = new PrintWriter(pyFile);
			pyFile_writer.print(code.render());
			pyFile_writer.close();
		}catch(FileNotFoundException e){
			System.out.println("File not Found");
		}
	}

 	@Override public void exitSize(TintaParser.SizeContext ctx) {
 		code.add("cor", backGroundColor);

 		// Rectangulo
 		if(ctx.getChild(3).getChildCount() == 0) {
			code.add("width", ctx.getChild(1).getText());
			code.add("height", ctx.getChild(3).getText());

			if(ctx.getChildCount() > 5)
	 		{
	 			code.remove("cor");
				code.add("cor", ctx.getChild(6).getChild(1).getText() );
				backGroundColor = ctx.getChild(6).getChild(1).getText();
	 		}
		}
		// Quadrado
		else{
			code.add("width", ctx.getChild(1).getText());
			code.add("height", ctx.getChild(1).getText());
			if(ctx.getChildCount() > 3)
	 		{
	 			code.remove("cor");
				code.add("cor", ctx.getChild(4).getChild(1).getText() );
				backGroundColor = ctx.getChild(4).getChild(1).getText();
	 		}
		}
	}

 	@Override public void exitName(TintaParser.NameContext ctx) {
 		// name was defined has "" at the beggining, so we start by removing that
 		code.remove("name");
 		// adding the new name
 		code.add("name", "\"" + ctx.getChild(1).getText() + "\"");
 	}

 	@Override public void exitReta(TintaParser.RetaContext ctx) {
 		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting the template and setting ORIGEM and DESTINO for RETA
 		ST st = group.getInstanceOf("draw_line");

 		// Getting ORIGEM pontos
 		Ponto pontoOrigem = getPonto(ctx , 1);
 		st.add("origemX", pontoOrigem.x );
	 	st.add("origemY", pontoOrigem.y );

	 	// Getting DESTINO pontos
 		Ponto pontoDestino = getPonto(ctx , 3);
 		st.add("destinoX", pontoDestino.x );
	 	st.add("destinoY", pontoDestino.y );

	 	// Getting Settings
	 	Settings s = getSettings(ctx, 5);
	 	st.add("color", s.c);
 		st.add("grossura", s.g);

 		// Saving the variable on a HashMap
 		actions.put(ctx.getParent().getChild(0).getText(), st);
 	}

 	@Override public void exitPontod(TintaParser.PontodContext ctx) {
 		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting template for circle in case users wishes to draw the dot
 		ST st = group.getInstanceOf("draw_circle");

 		// Getting name and x, y coords
 		String name = ctx.getParent().getChild(0).getText();
 		String x = ctx.getChild(1).getChild(1).getText();
 		String y = ctx.getChild(1).getChild(3).getText();

 		// Setting up template
 		st.add("X", x);
 		st.add("Y", y);
 		st.add("R", "int(width * height * 0.000005)" );
 		st.add("color", "(0,0,0)");
 		st.add("grossura", "int(width * height * 0.000005)" );

 		// Saving ponto in case it is called later
 		pontos.put(name, new Ponto(x, y) );
 		// Saving template in case users wishes to draw it
 		actions.put(name, st);

 	}

 	@Override public void exitCircle(TintaParser.CircleContext ctx){
 		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting template for circle
 		ST st = group.getInstanceOf("draw_circle");

 		// Getting pontos CENTRO
 		Ponto pontoCentro = getPonto(ctx , 1);
 		st.add("X", pontoCentro.x );
	 	st.add("Y", pontoCentro.y );

	 	st.add("R", ctx.getChild(3).getText());

	 	// Getting Settings
	 	Settings s = getSettings(ctx, 5);
	 	st.add("color", s.c);
 		st.add("grossura", s.g);

	 	// Saving the variable on a HashMap
 		actions.put(ctx.getParent().getChild(0).getText(), st);
 	}

 	@Override public void exitTriangle(TintaParser.TriangleContext ctx){
 		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting template for circle
 		ST st = group.getInstanceOf("draw_triangle");

 		// Getting pontos CENTRO
 		Ponto pontoCentro = getPonto(ctx , 1);
 		st.add("X", pontoCentro.x );
	 	st.add("Y", pontoCentro.y );

	 	st.add("R", ctx.getChild(3).getText());

	 	// Getting Settings
	 	Settings s = getSettings(ctx, 5);
	 	st.add("color", s.c);
 		st.add("grossura", s.g);

	 	// Saving the variable on a HashMap
 		actions.put(ctx.getParent().getChild(0).getText(), st);
 	}

	@Override public void exitRectangle(TintaParser.RectangleContext ctx) {
		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting template for rectangle
 		ST st = group.getInstanceOf("draw_rectangle");

 		// Getting ORIGEM pontos
 		Ponto pontoOrigem = getPonto(ctx , 1);
 		st.add("origemX", pontoOrigem.x );
	 	st.add("origemY", pontoOrigem.y );

	 	// Getting DESTINO pontos
 		Ponto pontoDestino = getPonto(ctx , 3);
 		st.add("destinoX", pontoDestino.x );
	 	st.add("destinoY", pontoDestino.y );

	 	// Getting Settings
	 	Settings s = getSettings(ctx, 5);
	 	st.add("color", s.c);
 		st.add("grossura", s.g);

	 	// Saving the variable on a HashMap
 		actions.put(ctx.getParent().getChild(0).getText(), st);

	}

	@Override public void exitEllipse(TintaParser.EllipseContext ctx) {
		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting template for ellipse
 		ST st = group.getInstanceOf("draw_ellipse");

 		// Getting ORIGEM pontos
 		Ponto pontoOrigem = getPonto(ctx , 1);
 		st.add("origemX", pontoOrigem.x );
	 	st.add("origemY", pontoOrigem.y );

	 	// Getting DESTINO pontos
 		Ponto pontoDestino = getPonto(ctx , 3);
 		st.add("destinoX", pontoDestino.x );
	 	st.add("destinoY", pontoDestino.y );

	 	st.add("angle", ctx.getChild(5).getText());
	 	st.add("startangle", ctx.getChild(7).getText());
	 	st.add("endangle", ctx.getChild(9).getText());

	 	// Getting Settings
	 	Settings s = getSettings(ctx, 11);
	 	st.add("color", s.c);
 		st.add("grossura", s.g);

	 	// Saving the variable on a HashMap
 		actions.put(ctx.getParent().getChild(0).getText(), st);
	}

	@Override public void exitPolygon(TintaParser.PolygonContext ctx) {

		String array = "";
		// Checking if varible is already defined
 		if(actions.containsKey(ctx.getParent().getChild(0).getText()))
 		{
 			errorPrint(1, ctx.getParent().getChild(0).getText());
 		}

 		// Getting template for polygon
 		ST st = group.getInstanceOf("draw_polygon");
 		int number_of_points = 1;
 		int flag=0;
 		// Obtendo o numero de pontos que o utilizador inseriu
 		while(!ctx.getChild(number_of_points).getText().equals("];")){number_of_points++;}
 		number_of_points = (number_of_points - 1) / 2 + 1;

 		for (int i = 1; i < ctx.getChildCount() - 1; i+=2 ) {
 			if (ctx.getChild(i).getText().equals("True") || ctx.getChild(i).getText().equals("False")) {
	 			st.add("bool", ctx.getChild(i).getText());
	 			flag=i+2;
	 			break;
 			}
 			else{
 				if(ctx.getChild(i).getText().contains(",")){
	 				if(i>2){
	 					String name = ctx.getChild(i-2).getText();
				 		if(pontos.containsKey(name)){
		 					String p2 = ",["+ctx.getChild(i).getChild(1).getText()+","+ctx.getChild(i).getChild(3).getText()+"]";
					 		st.add("array", p2 );
					 	}else{
					 		String p2 = "["+ctx.getChild(i).getChild(1).getText()+","+ctx.getChild(i).getChild(3).getText()+"]";
					 		st.add("array", p2 );
					 	}
					}else{
						String p2 = "["+ctx.getChild(i).getChild(1).getText()+","+ctx.getChild(i).getChild(3).getText()+"]";
					 	st.add("array", p2 );
					}
			 	}
			 	else{
	 				String name = ctx.getChild(i).getText();
			 		if(pontos.containsKey(name))
			 		{
			 			Ponto p = pontos.get(name);
			 			String p1 = ",["+p.x+","+p.y+"]";
			 			st.add("array",p1);
			 		}
			 		else
			 		{
			 			errorPrint(2, name);
			 		}
				}
 			}
 		}

 		// Getting Settings
	 	Settings s = getSettings(ctx, flag);
	 	st.add("color", s.c);
 		st.add("grossura", s.g);

	 	// Saving the variable on a HashMap
 		actions.put(ctx.getParent().getChild(0).getText(), st);
	}

 	@Override public void exitUpdatePoint(TintaParser.UpdatePointContext ctx) {
 		// Getting the variable name
 		String variableName = ctx.getChild(1).getText();

 		// Checking if variable exists
 		if(actions.containsKey(variableName))
 		{
 			ST st = actions.get(variableName);
 			Ponto p;
 			// Checking what type of variable it is (RETA, PONTO, ...)
 			switch(st.getName()){
 				case "/draw_line" : errorPrint(6, ""); break;
 				case "/draw_triangle" : st.remove("X"); st.remove("Y");
 										// Getting new point 
								 		p = getPonto(ctx , 4);
								 		st.add("X", p.x );
									 	st.add("Y", p.y );
 										actions.put(variableName, st);
 										break;
 				case "/draw_circle" : 	st.remove("X"); st.remove("Y");
 										// Getting new point 
								 		p = getPonto(ctx , 4);
								 		st.add("X", p.x );
									 	st.add("Y", p.y );
 										actions.put(variableName, st);
 										break;
 				case "/draw_rectangle" : errorPrint(6, ""); break;
 				case "/draw_ellipse" : errorPrint(6,""); break;
 				case "/draw_polygon" : errorPrint(6,""); break;
 				default : errorPrint(5, variableName); break;
 			}
 		}
 		else
 		{
 			errorPrint(2, variableName);
 		}
 	}

 	@Override public void exitUpdatePointReta(TintaParser.UpdatePointRetaContext ctx) {
 		// Getting the variable name
 		String variableName = ctx.getChild(1).getText();

 		// Checking if variable exists
 		if(actions.containsKey(variableName))
 		{
 			ST st = actions.get(variableName);
 			int picked_point;
 			Ponto p;
 			// Checking what type of variable it is (RETA, PONTO, ...)
 			switch(st.getName()){
 				case "/draw_line" : 
 									picked_point = Integer.parseInt(ctx.getChild(3).getText());
 									// Updating ORIGEM
 									if(picked_point == 1)
 									{
 										st.remove("origemX"); st.remove("origemY");
 										// Getting new point 
								 		p = getPonto(ctx , 5);
								 		st.add("origemX", p.x );
									 	st.add("origemY", p.y );
 									}
 									// Updating DESTINO
 									else if(picked_point == 2)
 									{
 										st.remove("destinoX"); st.remove("destinoY");
 										// Getting new point 
								 		p = getPonto(ctx , 5);
								 		st.add("destinoX", p.x );
									 	st.add("destinoY", p.y );
 									}
 									else
 									{
 										errorPrint(10, variableName + " only has 2");
 									}
 									actions.put(variableName, st);
 									break;

 				case "/draw_circle" : errorPrint(4, ""); break;
 				case "/draw_triangle" : errorPrint(4, ""); break;
 				case "/draw_rectangle" : 
 									picked_point = Integer.parseInt(ctx.getChild(3).getText());
 									// Updating ORIGEM
 									if(picked_point == 1)
 									{
 										st.remove("origemX"); st.remove("origemY");
 										// Getting new point 
								 		p = getPonto(ctx , 5);
								 		st.add("origemX", p.x );
									 	st.add("origemY", p.y );
 									}
 									// Updating DESTINO
 									else if(picked_point == 2)
 									{
 										st.remove("destinoX"); st.remove("destinoY");
 										// Getting new point 
								 		p = getPonto(ctx , 5);
								 		st.add("destinoX", p.x );
									 	st.add("destinoY", p.y );
 									}
 									else
 									{
 										errorPrint(10, variableName + " only has 2");
 									}
 									actions.put(variableName, st);
 									break;

 				case "/draw_ellipse" : 
 									picked_point = Integer.parseInt(ctx.getChild(3).getText());
 									// Updating ORIGEM
 									if(picked_point == 1)
 									{
 										st.remove("origemX"); st.remove("origemY");
 										// Getting new point 
								 		p = getPonto(ctx , 5);
								 		st.add("origemX", p.x );
									 	st.add("origemY", p.y );
 									}
 									// Updating DESTINO
 									else if(picked_point == 2)
 									{
 										st.remove("destinoX"); st.remove("destinoY");
 										// Getting new point 
								 		p = getPonto(ctx , 5);
								 		st.add("destinoX", p.x );
									 	st.add("destinoY", p.y );
 									}
 									else
 									{
 										errorPrint(10, variableName + " only has 2");
 									}
 									actions.put(variableName, st);
 									break;

 				case "/draw_polygon" : 	
 									// Obtendo Lista de pontos previamente definidas no Poly
 									AttributeList polyPoints = (AttributeList)  st.getAttribute("array");
 									int num_points = polyPoints.size();
 									picked_point = Integer.parseInt(ctx.getChild(3).getText());

 									// Checking if user is trying to update a point that doesnt exist
 									if(picked_point <= num_points)
 									{
 										// Se nao for o primeiro ponto, acrescentar virgula no inicio
 										String comma = "";
 										if(picked_point > 1) comma = ","; 
 										// Getting new point 
								 		p = getPonto(ctx , 5);
 										// Actualizar pontos no template
 										polyPoints.set(picked_point-1, comma + "[" + p.x + "," + p.y + "]");
 										st.remove("array");
 										st.add("array", polyPoints);
 									}
 									else
 									{
 										errorPrint(10, variableName + " only has " + Integer.toString(num_points));
 									}
 									actions.put(variableName, st);
 									break;
 									
 				default : errorPrint(5, variableName); break;
 			}
 		}
 		else
 		{
 			errorPrint(2, variableName);
 		}
 	}

 	@Override public void exitUpdateColor(TintaParser.UpdateColorContext ctx) {
 		// Getting the variable name
 		String variableName = ctx.getChild(1).getText();

 		// Checking if variable exists
 		if(actions.containsKey(variableName))
 		{
 			ST st = actions.get(variableName);
 			if(st.getName().equals("/definition"))
 			{
 				// if it is an definition it will have to change every object
 				AttributeList objects = (AttributeList)  st.getAttribute("object");
 				st.remove("object");
 				for(int i = 0; i < objects.size() ; i++)
 				{
 					ST tmp = (ST) objects.get(i);
	 				tmp.remove("color");
	 				tmp.add("color", ctx.getChild(5).getText());
	 				st.add("object", tmp);
 				}
 			}
 			else
 			{
	 			st.remove("color");
	 			st.add("color", ctx.getChild(5).getText());
 			}
 			actions.put(variableName, st);
 		}
 		else
 		{
 			errorPrint(2, variableName);
 		}
 	}
  @Override public void exitPaint(TintaParser.PaintContext ctx) {
    	// Getting the variable name
 		String variableName = ctx.getChild(1).getText();

 		// Checking if variable exists
 		if(actions.containsKey(variableName))
 		{
 			ST st = actions.get(variableName);
 			if(st.getName().equals("/definition"))
 			{
 				AttributeList objects = (AttributeList)  st.getAttribute("object");
 				st.remove("object");
 				for(int i = 0; i < objects.size() ; i++)
 				{
 					ST tmp = (ST) objects.get(i);
 					if(!tmp.getName().equals("/draw_line"))
 					{
	 					tmp.remove("grossura");
	 					tmp.add("grossura", "-1");
	 				}
 					st.add("object", tmp);
 				}
 			}
 			else
 			{
 				if(!st.getName().equals("/draw_line"))
 				{
	 				st.remove("grossura");
	 				st.add("grossura", "-1");
	 			}
 			}
 			actions.put(variableName, st);
 			code.add("actions", st.render());
 		}
 		else
 		{
 			errorPrint(2, variableName);
 		}
  }
 	@Override public void exitUpdateGrossura(TintaParser.UpdateGrossuraContext ctx) {
 		// Getting the variable name
 		String variableName = ctx.getChild(1).getText();

 		// Checking if variable exists
 		if(actions.containsKey(variableName))
 		{
 			ST st = actions.get(variableName);
 			if(st.getName().equals("/definition"))
 			{
 				// if it is an definition it will have to change every object
 				AttributeList objects = (AttributeList)  st.getAttribute("object");
 				st.remove("object");
 				for(int i = 0; i < objects.size() ; i++)
 				{
 					ST tmp = (ST) objects.get(i);
	 				tmp.remove("grossura");
	 				tmp.add("grossura", ctx.getChild(5).getText());
 					st.add("object", tmp);
 				}
 			}
 			else
 			{
	 			st.remove("grossura");
	 			st.add("grossura", ctx.getChild(5).getText());
 			}
 			actions.put(variableName, st);
 		}
 		else
 		{
 			errorPrint(2, variableName);
 		}
 	}

  @Override public void exitDefinition(TintaParser.DefinitionContext ctx) {
	    String class_name = ctx.getChild(0).getText();
      	int count_settings = 0;
      	boolean grossura = false;
      	boolean color   = false;
        int color_pos = 0;
        int grossura_pos = 0;

	    // Getting template for definition
 		ST st = group.getInstanceOf("definition");

      	//checking if there is color
      	if(ctx.getChild(2).getText().contains("(C=") && ctx.getChild(3).getText().contains("(C="))
      	{
          errorPrint(9, "color");
      	}
      	else
      	{
	        if(ctx.getChild(2).getText().contains("(C=")){
	          count_settings = count_settings + 1;
	          color = true;
            color_pos = 2;
	        }
	        if(ctx.getChild(3).getText().contains("(C=")){
	          count_settings = count_settings + 1;
	          color = true;
            color_pos = 3;
	        }
      }

      	//checking if there is thickness
      	if(ctx.getChild(2).getText().contains("(G=") && ctx.getChild(3).getText().contains("(G="))
      	{
          errorPrint(9, "thickness");
      	}
      	else
      	{
        	if(ctx.getChild(2).getText().contains("(G=")){
          		count_settings = count_settings + 1;
         		grossura = true;
            grossura_pos = 2;
        	}
        	if(ctx.getChild(3).getText().contains("(G=")){
          		count_settings = count_settings + 1;
         		grossura = true;
            grossura_pos = 3;
        	}
      	}

		// Appending every object on the definition
	    for(int i = 2 + count_settings ; i < ctx.getChildCount() - 1 ; i++){
	    	String var_name = ctx.getChild(i).getChild(0).getText();

	    	// Checking if variable exists
        	ST tmp;
	    	if( actions.containsKey(var_name) )
	 		{
        		tmp = actions.get(var_name);
        		if(color){
          			   tmp.remove("color");
     				       tmp.add("color", ctx.getChild(color_pos).getChild(0).getChild(1).getText());
        		}
        		if(grossura){
          			   tmp.remove("grossura");
         			     tmp.add("grossura", ctx.getChild(grossura_pos).getChild(0).getChild(1).getText());
        		}

        		st.add("object", tmp );
	 		}
	 		else
	 		{
	 			errorPrint(2, var_name);
	 		}
	    }

	    actions.put(class_name, st);
  	}

	@Override public void exitRgbcolor(TintaParser.RgbcolorContext ctx) {

	    //Tries to convert data to Integer, Error if format is wrong.
	    int int1 = -1;
	    int int2 = -1;
	    int int3 = -1;

	    try {
	      int1 = Integer.parseInt(ctx.getChild(1).getText());
	      int2 = Integer.parseInt(ctx.getChild(3).getText());
	      int3 = Integer.parseInt(ctx.getChild(5).getText());
	    } catch (NumberFormatException e) {
	      errorPrint(7, "");
	    }

	    //Cheching RGB range
	    String error = "";
	    if(int1 < 0 || int1 > 255){
	      error += "<R> COLOR";
	    }
	    if(int2 < 0 || int2 > 255){
	      error += "<G> COLOR";
	    }
	    if(int3 < 0 || int3 > 255){
	      error += "<B> COLOR";
	    }

	    if(error.length() > 0){
	      errorPrint(8, error);
	    }

  }

  	@Override public void exitDraw(TintaParser.DrawContext ctx) {
 		// Checking if variable is defined
 		if(actions.containsKey(ctx.getChild(1).getText()))
 		{
 			code.add("actions", actions.get(ctx.getChild(1).getText()).render());
 		}
 		else
 		{
 			errorPrint(2, ctx.getChild(1).getText());
 		}
 	}

 	@Override public void exitDelete(TintaParser.DeleteContext ctx){
 		// Checking if variable is defined
 		String variableName = ctx.getChild(1).getText();
 		if(actions.containsKey(variableName))
 		{
 			ST st = actions.get(variableName);
 			if(st.getName().equals("/definition"))
 			{
 				AttributeList objects = (AttributeList)  st.getAttribute("object");
 				st.remove("object");
 				for(int i = 0; i < objects.size() ; i++)
 				{
 					ST tmp = (ST) objects.get(i);
 					tmp.remove("color");
	 				tmp.add("color", backGroundColor);
 					st.add("object", tmp);
 				}
 			}
 			else
 			{
				st.remove("color");
	 			st.add("color", backGroundColor);		
 			}
 			code.add("actions", st.render());
 		}
 		else
 		{
 			errorPrint(2, ctx.getChild(1).getText());
 		}
 	}

 	public void errorPrint(int error, String message)
 	{
 		switch(error){
 			case 1 : System.err.println("ERROR: \""+ message + "\" is already defined, use UPDATE instead."); break;
 			case 2 : System.err.println("ERROR: \""+ message + "\" not defined!"); break;
 			case 3 : System.err.println("ERROR: Something went wrong at defining \"" + message + "\""); break;
 			case 4 : System.err.println("ERROR: You can't update 'p1'/'p2' of POINT/CIRCLE/TRIANGLE, use p instead."); break;
 			case 5 : System.err.println("ERROR: Something went wrong at updating \"" + message + "\""); break;
 			case 6:  System.err.println("ERROR: You can't update 'p' of RETA/RECTANGLE/ELLIPSE/POLYGON, use p1 or p2 instead."); break;
      		case 7:  System.err.println("ERROR: Make certain you are using a color in the format INT, INT, INT ."); break;
      		case 8:  System.err.println("ERROR: " + message + " integer out of range, be sure to insert a number between 0 and 255."); break;
      		case 9:	 System.err.println("ERROR: Two "+ message +" settings in row."); break;
      		case 10:  System.err.println("ERROR: Invalid point index, "+ message + " points."); break;
 			default : break;
 		}

 		System.exit(0);
 	}

}
