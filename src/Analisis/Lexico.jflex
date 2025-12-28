package Analisis;

import java_cup.runtime.Symbol;
import java.util.LinkedList;
import Excepciones.Errores;

%%
// codigo de usuario
%{
   public LinkedList<Errores> listaErrores = new LinkedList<>();
%}

%init{
    yyline = 1;
    yycolumn = 1;
    listaErrores = new LinkedList<>();
%init}

%cup
%class scanner
%public 
%line
%char
%column
%full
%ignorecase

PAR1 = "("
PAR2 = ")"
LLAVE1 = "{"
LLAVE2 = "}"
CORCHETE1 = "["
CORCHETE2 = "]"
FINCADENA = ";"
COMA = ","
PUNTO = "."
MAS = "+"
MENOS = "-"
MULTI = "*"
DIV = "/"
MOD = "%"
POT = "**"
IGUAL = "="
EQUALS = "=="
DIFERENTE = "!="
MENORIGUAL = "<="
MAYORIGUAL = ">="
MENORQ = "<"
MAYORQ = ">"
OR = "||"
AND = "&&"
XOR = "^"
NOT = "!"
DOSPUNTOS = ":"
BLANCOS = [\ \r\t\f\n]+
COMENTARIO_LINEA = "//"[^\r\n]*
COMENTARIO_MULTILINEA = "/*"([^*]|[\r\n]|("*"+([^*/]|[\r\n])))*"*"+"/"
ENTERO = [0-9]+
DECIMAL = [0-9]+"."[0-9]+
CHAR = \'([^\'\\]|\\.)\'
CADENA = \"([^\"\\]|\\.)*\"
ID = [a-zA-Z_][a-zA-Z0-9_]*
TRUE = "true"
FALSE = "false"
PRINT = "print"
VAR = "var"
INT = "int"
DOUBLE = "double"
BOOL = "bool"
CHAR_TYPE = "char"
STRING = "string"
_if = "if"
_else = "else"
_while = "while"
_for = "for"
_do = "do"
_break = "break"
_continue = "continue"
_switch = "switch"
_case = "case"
_default = "default"
_list = "list"
_new = "new"
_append = "append"
_remove = "remove"
INCREMENTO = "++"
DECREMENTO = "--"
ROUND = "round"
LENGTH = "length"
TOSTRING = "tostring"
FIND = "find"
RETURN = "return"
VOID = "void"
START = "start"

%%

<YYINITIAL> {BLANCOS} { }
<YYINITIAL> {COMENTARIO_LINEA} { }
<YYINITIAL> {COMENTARIO_MULTILINEA} { }

<YYINITIAL> {PRINT} {return new Symbol(sym.PRINT, yyline, yycolumn, yytext());}
<YYINITIAL> {VAR} {return new Symbol(sym.VAR, yyline, yycolumn, yytext());}
<YYINITIAL> {INT} {return new Symbol(sym.INT, yyline, yycolumn, yytext());}
<YYINITIAL> {DOUBLE} {return new Symbol(sym.DOUBLE, yyline, yycolumn, yytext());}
<YYINITIAL> {BOOL} {return new Symbol(sym.BOOL, yyline, yycolumn, yytext());}
<YYINITIAL> {CHAR_TYPE} {return new Symbol(sym.CHAR_TYPE, yyline, yycolumn, yytext());}
<YYINITIAL> {STRING} {return new Symbol(sym.STRING, yyline, yycolumn, yytext());}
<YYINITIAL> {TRUE} {return new Symbol(sym.TRUE, yyline, yycolumn, yytext());}
<YYINITIAL> {FALSE} {return new Symbol(sym.FALSE, yyline, yycolumn, yytext());}
<YYINITIAL> {_if} {return new Symbol(sym._if, yyline, yycolumn, yytext());}
<YYINITIAL> {_else} {return new Symbol(sym._else, yyline, yycolumn, yytext());}
<YYINITIAL> {_while} {return new Symbol(sym._while, yyline, yycolumn, yytext());}
<YYINITIAL> {_for} {return new Symbol(sym._for, yyline, yycolumn, yytext());}
<YYINITIAL> {_do} {return new Symbol(sym._do, yyline, yycolumn, yytext());}
<YYINITIAL> {_break} {return new Symbol(sym._break, yyline, yycolumn, yytext());}
<YYINITIAL> {_continue} {return new Symbol(sym._continue, yyline, yycolumn, yytext());}
<YYINITIAL> {_switch} {return new Symbol(sym._switch, yyline, yycolumn, yytext());}
<YYINITIAL> {_case} {return new Symbol(sym._case, yyline, yycolumn, yytext());}
<YYINITIAL> {_default} {return new Symbol(sym._default, yyline, yycolumn, yytext());}
<YYINITIAL> {_list} {return new Symbol(sym._list, yyline, yycolumn, yytext());}
<YYINITIAL> {_new} {return new Symbol(sym._new, yyline, yycolumn, yytext());}
<YYINITIAL> {_append} {return new Symbol(sym._append, yyline, yycolumn, yytext());}
<YYINITIAL> {_remove} {return new Symbol(sym._remove, yyline, yycolumn, yytext());}
<YYINITIAL> {INCREMENTO} {return new Symbol(sym.INCREMENTO, yyline, yycolumn, yytext());}
<YYINITIAL> {DECREMENTO} {return new Symbol(sym.DECREMENTO, yyline, yycolumn, yytext());}
<YYINITIAL> {ROUND} {return new Symbol(sym.ROUND, yyline, yycolumn, yytext());}
<YYINITIAL> {LENGTH} {return new Symbol(sym.LENGTH, yyline, yycolumn, yytext());}
<YYINITIAL> {TOSTRING} {return new Symbol(sym.TOSTRING, yyline, yycolumn, yytext());}
<YYINITIAL> {FIND} {return new Symbol(sym.FIND, yyline, yycolumn, yytext());}
<YYINITIAL> {RETURN} {return new Symbol(sym.RETURN, yyline, yycolumn, yytext());}
<YYINITIAL> {VOID} {return new Symbol(sym.VOID, yyline, yycolumn, yytext());}
<YYINITIAL> {START} {return new Symbol(sym.START, yyline, yycolumn, yytext());}
<YYINITIAL> {DECIMAL} {return new Symbol(sym.DECIMAL, yyline, yycolumn, yytext());}
<YYINITIAL> {ENTERO} {return new Symbol(sym.ENTERO, yyline, yycolumn, yytext());}
<YYINITIAL> {ID} {return new Symbol(sym.ID, yyline, yycolumn, yytext());}

<YYINITIAL> {CHAR} {
    String texto = yytext();
    char contenido;
    
    // Verificar si es una secuencia de escape
    if (texto.length() == 4 && texto.charAt(1) == '\\') {
        char escape = texto.charAt(2);
        contenido = switch (escape) {
            case 'n' -> '\n';
            case 't' -> '\t';
            case '\\' -> '\\';
            case '\'' -> '\'';
            case '"' -> '"';
            default -> escape;
        };
    } else {
        contenido = texto.charAt(1);
    }
    
    return new Symbol(sym.CHAR, yyline, yycolumn, contenido);
}

<YYINITIAL> {CADENA} {
    String cadena = yytext();
    cadena = cadena.substring(1, cadena.length()-1);
    
    // Procesar secuencias de escape
    cadena = cadena.replace("\\n", "\n");
    cadena = cadena.replace("\\t", "\t");
    cadena = cadena.replace("\\\"", "\"");
    cadena = cadena.replace("\\'", "'");
    cadena = cadena.replace("\\\\", "\\");
    
    return new Symbol(sym.CADENA, yyline, yycolumn, cadena);
}

<YYINITIAL> {PAR1} {return new Symbol(sym.PAR1, yyline, yycolumn, yytext());}
<YYINITIAL> {PAR2} {return new Symbol(sym.PAR2, yyline, yycolumn, yytext());}
<YYINITIAL> {DOSPUNTOS} {return new Symbol(sym.DOSPUNTOS, yyline, yycolumn, yytext());}
<YYINITIAL> {LLAVE1} {return new Symbol(sym.LLAVE1, yyline, yycolumn, yytext());}
<YYINITIAL> {LLAVE2} {return new Symbol(sym.LLAVE2, yyline, yycolumn, yytext());}
<YYINITIAL> {CORCHETE1} {return new Symbol(sym.CORCHETE1, yyline, yycolumn, yytext());}
<YYINITIAL> {CORCHETE2} {return new Symbol(sym.CORCHETE2, yyline, yycolumn, yytext());}
<YYINITIAL> {COMA} {return new Symbol(sym.COMA, yyline, yycolumn, yytext());}
<YYINITIAL> {PUNTO} {return new Symbol(sym.PUNTO, yyline, yycolumn, yytext());}
<YYINITIAL> {FINCADENA} {return new Symbol(sym.FINCADENA, yyline, yycolumn, yytext());}

<YYINITIAL> {POT} {return new Symbol(sym.POT, yyline, yycolumn, yytext());}
<YYINITIAL> {MAS} {return new Symbol(sym.MAS, yyline, yycolumn, yytext());}
<YYINITIAL> {MENOS} {return new Symbol(sym.MENOS, yyline, yycolumn, yytext());}
<YYINITIAL> {MULTI} {return new Symbol(sym.MULTI, yyline, yycolumn, yytext());}
<YYINITIAL> {DIV} {return new Symbol(sym.DIV, yyline, yycolumn, yytext());}
<YYINITIAL> {MOD} {return new Symbol(sym.MOD, yyline, yycolumn, yytext());}
<YYINITIAL> {OR} {return new Symbol(sym.OR, yyline, yycolumn, yytext());}
<YYINITIAL> {AND} {return new Symbol(sym.AND, yyline, yycolumn, yytext());}
<YYINITIAL> {XOR} {return new Symbol(sym.XOR, yyline, yycolumn, yytext());}
<YYINITIAL> {DIFERENTE} {return new Symbol(sym.DIFERENTE, yyline, yycolumn, yytext());}
<YYINITIAL> {MENORIGUAL} {return new Symbol(sym.MENORIGUAL, yyline, yycolumn, yytext());}
<YYINITIAL> {MAYORIGUAL} {return new Symbol(sym.MAYORIGUAL, yyline, yycolumn, yytext());}
<YYINITIAL> {EQUALS} {return new Symbol(sym.EQUALS, yyline, yycolumn, yytext());}
<YYINITIAL> {MENORQ} {return new Symbol(sym.MENORQ, yyline, yycolumn, yytext());}
<YYINITIAL> {MAYORQ} {return new Symbol(sym.MAYORQ, yyline, yycolumn, yytext());}
<YYINITIAL> {NOT} {return new Symbol(sym.NOT, yyline, yycolumn, yytext());}
<YYINITIAL> {IGUAL} {return new Symbol(sym.IGUAL, yyline, yycolumn, yytext());}

<YYINITIAL> . {
    listaErrores.add(new Errores("LEXICO", "El caracter '"+yytext()+"' no pertenece al lenguaje", yyline, yycolumn));
}
