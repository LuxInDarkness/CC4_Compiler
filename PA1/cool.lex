/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%state STRING MULTILINE
%cup

alphanumeric = [a-zA-Z0-9_]
lowercase = [a-z]
uppercase = [A-Z]
digit = [0-9]
%%

<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

<YYINITIAL>"+"          { return new Symbol(TokenConstants.PLUS); }

<YYINITIAL>"-"          { return new Symbol(TokenConstants.MINUS); }

<YYINITIAL>"*"          { return new Symbol(TokenConstants.MULT); }

<YYINITIAL>"/"          { return new Symbol(TokenConstants.DIV); }

<YYINITIAL>"~"          { return new Symbol(TokenConstants.NEG); }

<YYINITIAL>"<"          { return new Symbol(TokenConstants.LT); }

<YYINITIAL>"<="          { return new Symbol(TokenConstants.LE); }

<YYINITIAL>"="          { return new Symbol(TokenConstants.EQ); }

<YYINITIAL>"{"          { return new Symbol(TokenConstants.LBRACE); }

<YYINITIAL>"}"          { return new Symbol(TokenConstants.RBRACE); }

<YYINITIAL>":"          { return new Symbol(TokenConstants.COLON); }

<YYINITIAL>";"          { return new Symbol(TokenConstants.SEMI); }

<YYINITIAL>"("          { return new Symbol(TokenConstants.LPAREN); }

<YYINITIAL>")"          { return new Symbol(TokenConstants.RPAREN); }

<YYINITIAL>"+"          { return new Symbol(TokenConstants.PLUS); }

<YYINITIAL>"<-"          { return new Symbol(TokenConstants.ASSIGN); }

<YYINITIAL>","          { return new Symbol(TokenConstants.COMMA); }

<YYINITIAL>"."          { return new Symbol(TokenConstants.DOT); }

<YYINITIAL>[Cc][Ll][Aa][Ss][Ss]          { return new Symbol(TokenConstants.CLASS); }

<YYINITIAL>[Ee][Ll][Ss][Ee]          { return new Symbol(TokenConstants.ELSE); }

<YYINITIAL>[Nn][Oo][Tt]          { return new Symbol(TokenConstants.NOT); }

<YYINITIAL>{digit}+          { return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }

<YYINITIAL>[t][Rr][Uu][Ee]          { return new Symbol(TokenConstants.BOOL_CONST, true); }

<YYINITIAL>[f][Aa][Ll][Ss][Ee]          { return new Symbol(TokenConstants.BOOL_CONST, false); }

<YYINITIAL>[Ff][Ii]          { return new Symbol(TokenConstants.FI); }

<YYINITIAL>[Ii][Ff]          { return new Symbol(TokenConstants.IF); }

<YYINITIAL>[Ii][Nn]          { return new Symbol(TokenConstants.IN); }

<YYINITIAL>[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]          { return new Symbol(TokenConstants.INHERITS); }

<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd]          { return new Symbol(TokenConstants.ISVOID); }

<YYINITIAL>[Ll][Ee][Tt]          { return new Symbol(TokenConstants.LET); }

<YYINITIAL>[Ll][Oo][Oo][Pp]          { return new Symbol(TokenConstants.LOOP); }

<YYINITIAL>[Pp][Oo][Oo][Ll]          { return new Symbol(TokenConstants.POOL); }

<YYINITIAL>[Tt][Hh][Ee][Nn]          { return new Symbol(TokenConstants.THEN); }

<YYINITIAL>[Ww][Hh][Ii][Ll][Ee]          { return new Symbol(TokenConstants.WHILE); }

<YYINITIAL>[Cc][Aa][Ss][Ee]          { return new Symbol(TokenConstants.CASE); }

<YYINITIAL>[Ee][Ss][Aa][Cc]          { return new Symbol(TokenConstants.ESAC); }

<YYINITIAL>[Nn][Ee][Ww]          { return new Symbol(TokenConstants.NEW); }

<YYINITIAL>[Oo][Ff]          { return new Symbol(TokenConstants.OF); }

<YYINITIAL>{lowercase}{alphanumeric}*          { return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }

<YYINITIAL>{uppercase}{alphanumeric}*          { return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }

<YYINITIAL>[\n]          { curr_lineno++; }

<YYINITIAL>[\"]              { yybegin(STRING); }

<STRING>[\"]                 { yybegin(YYINITIAL); }

//<STRING>[\\+\\n] { curr_lineno++; }

<STRING>[a-zA-Z0-9_+-/*. ]*[\\+n]*              { return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext())); }

<YYINITIAL>[\t\f\r\v ]+           { }

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
