grammar Tinta;

inputDescription : (size | name) actions EOF;

size : 'TAMANHO (' INT (')' | (':' INT ')')) (';' color)?;

name : 'NOME' STRINGPLUSINT;

actions : ( definition | object | update | draw | delete | paint)* ;

definition :  STRINGPLUSINT 'is {' settings? settings? (declared_object | object)+   '}';

declared_object : STRINGPLUSINT;

object : STRINGPLUSINT 'is' (reta | pontod | circle | triangle | rectangle | ellipse | polygon);

reta : 'RETA[' ( p1=ponto | STRINGPLUSINT ) ';' ( p2=ponto | STRINGPLUSINT ) (';' settings)? (';' settings)? ']';

pontod: 'PONTO[' ponto ']';

circle: 'CIRCULO[' ( p=ponto | STRINGPLUSINT ) ';' INT (';' settings)? (';' settings)? ']';

triangle: 'TRIANGULO[' ( p=ponto | STRINGPLUSINT) ';' INT (';' settings)? (';' settings)? ']';

rectangle: 'RECTANGULO[' (p1=ponto | STRINGPLUSINT) ';' ( p2=ponto | STRINGPLUSINT ) (';' settings)? (';' settings)? ']';

ellipse: 'ELIPSE[' (p1=ponto | STRINGPLUSINT) ';' ( p2=ponto | STRINGPLUSINT ) ';' INT ';' INT ';' INT (';' settings)? (';' settings)? ']';

polygon: 'POLIGONO[[' (ponto | STRINGPLUSINT) (',' (ponto | STRINGPLUSINT) )* '];' BOOL (';' settings)? (';' settings)? ']';

ponto: '(' INT ',' INT ')';

grossura: '(G=' INT ')';

color : '(C=' rgbcolor ')';

settings : 	grossura |	color;

update : 'UPDATE' STRINGPLUSINT '.p' '='  ( ponto | STRINGPLUSINT )  		#updatePoint
	|	 'UPDATE' STRINGPLUSINT '.p' INT '=' ( ponto | STRINGPLUSINT )   	#updatePointReta
	|	 'UPDATE' STRINGPLUSINT '.' 'C' '=' rgbcolor 		 				#updateColor
	|	 'UPDATE' STRINGPLUSINT '.' 'G' '=' INT 			 				#updateGrossura
	;

paint : 'PINTAR' STRINGPLUSINT;

draw : 'DESENHAR' STRINGPLUSINT;

delete: 'APAGAR' STRINGPLUSINT;

rgbcolor: '(' INT ',' INT ',' INT ')';


INT: [0-9]+;
BOOL: 'True' | 'False';
STRINGPLUSINT : [a-zA-Z] [a-zA-Z0-9]* ;
WS : [ \t\r\n]+ -> skip ;
COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip;
