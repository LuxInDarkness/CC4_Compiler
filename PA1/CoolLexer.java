/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int MULTILINE = 2;
	private final int yy_state_dtrans[] = {
		0,
		47,
		97
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NOT_ACCEPT,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"61:9,62,55,61,62,60,61:18,59,61,56,61:5,13,14,5,3,15,4,16,6,25:10,11,12,8,1" +
",2,61:2,19,54,17,34,21,30,57,32,31,54:2,18,54,22,23,35,54,27,20,24,28,33,36" +
",54:3,61,58,61:2,53,61,37,38,39,40,41,29,38,42,43,38:2,44,38,45,46,47,38,48" +
",49,26,50,51,52,38:3,9,61,10,7,61,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,167,
"0,1,2,1:4,3,1,4,1:8,5,6,7,1:2,8,1:3,9,10,11,9:9,10,9:2,10,9:3,1,12,1,13,3,1" +
"4,15,10,9,16,10:14,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35" +
",36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60" +
",61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85" +
",86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,9,10,103,104,105,106" +
",107,108,109,110,111")[0];

	private int yy_nxt[][] = unpackFromString(112,63,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,115,156:2,158,118,51,160,19,20" +
",156:2,52,72,76,156:3,162,164,157:2,159,157,161,157,73,116,119,77,163,157:3" +
",81,165,3,156,21,22,156,3,23:2,3,23,-1:65,24,-1:66,49,-1:57,25,-1:2,26,-1:7" +
"5,156,166,120,156:17,120,156:6,166,156:10,-1:2,156,-1:30,19,-1:54,157:10,12" +
"1,157:4,123,157:9,123,157:5,121,157:6,-1:2,157,-1:56,23,-1:7,23:2,-1,23,-1:" +
"17,156:38,-1:2,156,-1:22,157:38,-1:2,157,-1:22,156:15,140,156:9,140,156:12," +
"-1:2,156,-1:5,1,3:2,70:3,117,3:8,70:40,-1,48,70,74,70,-1,3:2,-1:8,71,-1:71," +
"156:12,27:2,156:24,-1:2,156,-1:22,157:2,125,157:11,28,157:5,125,157:5,28,15" +
"7:11,-1:2,157,-1:22,157:15,149,157:9,149,157:12,-1:2,157,-1:8,70:4,-1:8,70:" +
"40,-1:2,70,74,70,-1:23,75,-1:59,156:14,54,156:11,54,156:11,-1:2,156,-1:22,1" +
"57:3,135,157,55,157:6,56:2,157:14,55,157:3,135,157:5,-1:2,157,-1:8,74,-1:41" +
",74,-1:12,74,-1:28,79,-1:55,156:3,130,156,29,156:6,30:2,156:14,29,156:3,130" +
",156:5,-1:2,156,-1:22,157:12,53:2,157:24,-1:2,157,-1:8,70:4,-1,71,-1:6,70:4" +
"0,-1:2,70,74,70,-1:30,82,-1:52,156:7,31,156,31,156:28,-1:2,156,-1:22,157:34" +
",81,157:3,-1:2,157,-1,23:2,-1,23,-1:31,85,-1:48,156:19,32,156:15,32,156:2,-" +
"1:2,156,-1:22,157:7,57,157,57,157:28,-1:2,157,-1:27,88,-1:57,156:7,33,156,3" +
"3,156:28,-1:2,156,-1:22,157:19,58,157:15,58,157:2,-1:2,157,-1:62,91,-1:22,1" +
"56:4,34,156:19,34,156:13,-1:2,156,-1:22,157:7,59,157,59,157:28,-1:2,157,-1:" +
"7,94,-1:77,156:18,35,156:11,35,156:7,-1:2,156,-1:22,157:4,39,157:19,39,157:" +
"13,-1:2,157,-1:8,46,-1:41,46,-1:12,46,-1:21,156:4,36,156:19,36,156:13,-1:2," +
"156,-1:22,157:5,64,157:22,64,157:9,-1:2,157,-1:5,1,3:5,50,3:48,-1,3:4,-1,3:" +
"2,-1:17,37,156:21,37,156:15,-1:2,156,-1:22,157:4,60,157:19,60,157:13,-1:2,1" +
"57,-1:22,156:5,38,156:22,38,156:9,-1:2,156,-1:22,157:4,62,157:19,62,157:13," +
"-1:2,157,-1:22,156,40,156:25,40,156:10,-1:2,156,-1:22,63,157:21,63,157:15,-" +
"1:2,157,-1:22,156:3,41,156:28,41,156:5,-1:2,156,-1:22,157:18,61,157:11,61,1" +
"57:7,-1:2,157,-1:22,156:4,43,156:19,43,156:13,-1:2,156,-1:22,157,65,157:25," +
"65,157:10,-1:2,157,-1:22,156:17,44,156:5,44,156:14,-1:2,156,-1:22,157:4,42," +
"157:19,42,157:13,-1:2,157,-1:22,156:3,45,156:28,45,156:5,-1:2,156,-1:22,157" +
":3,66,157:28,66,157:5,-1:2,157,-1:22,157:4,67,157:19,67,157:13,-1:2,157,-1:" +
"22,157:17,68,157:5,68,157:14,-1:2,157,-1:22,157:3,69,157:28,69,157:5,-1:2,1" +
"57,-1:22,156:4,80,156,122,156:17,80,156:4,122,156:8,-1:2,156,-1:22,157:4,84" +
",157,137,157:17,84,157:4,137,157:8,-1:2,157,-1:8,70:3,78,-1:8,70:40,-1:2,70" +
",74,70,-1:20,156:4,83,156,86,156:17,83,156:4,86,156:8,-1:2,156,-1:22,157:4," +
"87,157,90,157:17,87,157:4,90,157:8,-1:2,157,-1:22,156:3,89,156:28,89,156:5," +
"-1:2,156,-1:22,157:11,93,157:21,93,157:4,-1:2,157,-1:22,156:6,92,156:22,92," +
"156:8,-1:2,156,-1:22,157:4,96,157:19,96,157:13,-1:2,157,-1:22,156:3,95,156:" +
"28,95,156:5,-1:2,156,-1:22,157,143,157:25,143,157:10,-1:2,157,-1:22,156:2,9" +
"8,156:17,98,156:17,-1:2,156,-1:22,157:2,145,157:17,145,157:17,-1:2,157,-1:2" +
"2,156:4,100,156:19,100,156:13,-1:2,156,-1:22,157:3,99,157:28,99,157:5,-1:2," +
"157,-1:22,156:16,138,156:17,138,156:3,-1:2,156,-1:22,157:3,101,157:28,101,1" +
"57:5,-1:2,157,-1:22,156:6,102,156:22,102,156:8,-1:2,156,-1:22,157:2,103,157" +
":17,103,157:17,-1:2,157,-1:22,156:14,142,156:11,142,156:11,-1:2,156,-1:22,1" +
"57:16,147,157:17,147,157:3,-1:2,157,-1:22,156:3,104,156:28,104,156:5,-1:2,1" +
"56,-1:22,157:6,105,157:22,105,157:8,-1:2,157,-1:22,156:6,144,156:22,144,156" +
":8,-1:2,156,-1:22,157:6,107,157:22,107,157:8,-1:2,157,-1:22,156:4,146,156:1" +
"9,146,156:13,-1:2,156,-1:22,157:14,151,157:11,151,157:11,-1:2,157,-1:22,156" +
",106,156:25,106,156:10,-1:2,156,-1:22,157:3,109,157:28,109,157:5,-1:2,157,-" +
"1:22,156:14,108,156:11,108,156:11,-1:2,156,-1:22,157:3,111,157:28,111,157:5" +
",-1:2,157,-1:22,156:10,148,156:20,148,156:6,-1:2,156,-1:22,157:6,152,157:22" +
",152,157:8,-1:2,157,-1:22,156:14,150,156:11,150,156:11,-1:2,156,-1:22,157:4" +
",153,157:19,153,157:13,-1:2,157,-1:22,156:7,110,156,110,156:28,-1:2,156,-1:" +
"22,157,112,157:25,112,157:10,-1:2,157,-1:22,157:14,113,157:11,113,157:11,-1" +
":2,157,-1:22,157:10,154,157:20,154,157:6,-1:2,157,-1:22,157:14,155,157:11,1" +
"55,157:11,-1:2,157,-1:22,157:7,114,157,114,157:28,-1:2,157,-1:22,156,124,15" +
"6,126,156:23,124,156:4,126,156:5,-1:2,156,-1:22,157,127,129,157:17,129,157:" +
"6,127,157:10,-1:2,157,-1:22,156:15,128,156:9,128,156:12,-1:2,156,-1:22,157," +
"131,157,133,157:23,131,157:4,133,157:5,-1:2,157,-1:22,156:6,132,156:22,132," +
"156:8,-1:2,156,-1:22,157:6,139,157:22,139,157:8,-1:2,157,-1:22,156:15,134,1" +
"56:9,134,156:12,-1:2,156,-1:22,157:15,141,157:9,141,157:12,-1:2,157,-1:22,1" +
"56:2,136,156:17,136,156:17,-1:2,156,-1:5");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.EQ); }
					case -3:
						break;
					case 3:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.PLUS); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MINUS); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.MULT); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.DIV); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.NEG); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.LT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.COLON); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.SEMI); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.COMMA); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.DOT); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -21:
						break;
					case 21:
						{ curr_lineno++; }
					case -22:
						break;
					case 22:
						{ yybegin(STRING); }
					case -23:
						break;
					case 23:
						{ }
					case -24:
						break;
					case 24:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.LE); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.OF); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.FI); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.IN); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.IF); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.LET); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.NEW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.NOT); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.CASE); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LOOP); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.ELSE); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.ESAC); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.THEN); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.POOL); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.CLASS); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.WHILE); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -46:
						break;
					case 46:
						{ curr_lineno++; }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext())); }
					case -48:
						break;
					case 48:
						{ yybegin(YYINITIAL); }
					case -49:
						break;
					case 50:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -50:
						break;
					case 51:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -51:
						break;
					case 52:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -52:
						break;
					case 53:
						{ return new Symbol(TokenConstants.OF); }
					case -53:
						break;
					case 54:
						{ return new Symbol(TokenConstants.FI); }
					case -54:
						break;
					case 55:
						{ return new Symbol(TokenConstants.IN); }
					case -55:
						break;
					case 56:
						{ return new Symbol(TokenConstants.IF); }
					case -56:
						break;
					case 57:
						{ return new Symbol(TokenConstants.LET); }
					case -57:
						break;
					case 58:
						{ return new Symbol(TokenConstants.NEW); }
					case -58:
						break;
					case 59:
						{ return new Symbol(TokenConstants.NOT); }
					case -59:
						break;
					case 60:
						{ return new Symbol(TokenConstants.CASE); }
					case -60:
						break;
					case 61:
						{ return new Symbol(TokenConstants.LOOP); }
					case -61:
						break;
					case 62:
						{ return new Symbol(TokenConstants.ELSE); }
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.ESAC); }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.THEN); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.POOL); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.CLASS); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.WHILE); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext())); }
					case -70:
						break;
					case 72:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -71:
						break;
					case 73:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -72:
						break;
					case 74:
						{ return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext())); }
					case -73:
						break;
					case 76:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -74:
						break;
					case 77:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -75:
						break;
					case 78:
						{ return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext())); }
					case -76:
						break;
					case 80:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -77:
						break;
					case 81:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -78:
						break;
					case 83:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -79:
						break;
					case 84:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -80:
						break;
					case 86:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -81:
						break;
					case 87:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -82:
						break;
					case 89:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -83:
						break;
					case 90:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -84:
						break;
					case 92:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -85:
						break;
					case 93:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -86:
						break;
					case 95:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -87:
						break;
					case 96:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -88:
						break;
					case 98:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -89:
						break;
					case 99:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -90:
						break;
					case 100:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -91:
						break;
					case 101:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -92:
						break;
					case 102:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -93:
						break;
					case 103:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -94:
						break;
					case 104:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -95:
						break;
					case 105:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -96:
						break;
					case 106:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -97:
						break;
					case 107:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -98:
						break;
					case 108:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -99:
						break;
					case 109:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -100:
						break;
					case 110:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -101:
						break;
					case 111:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -102:
						break;
					case 112:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -103:
						break;
					case 113:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -104:
						break;
					case 114:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -105:
						break;
					case 115:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -106:
						break;
					case 116:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -107:
						break;
					case 117:
						{ return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext())); }
					case -108:
						break;
					case 118:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -109:
						break;
					case 119:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -110:
						break;
					case 120:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -111:
						break;
					case 121:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -112:
						break;
					case 122:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -113:
						break;
					case 123:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -114:
						break;
					case 124:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -115:
						break;
					case 125:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -116:
						break;
					case 126:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -117:
						break;
					case 127:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -118:
						break;
					case 128:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -119:
						break;
					case 129:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -120:
						break;
					case 130:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -121:
						break;
					case 131:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -122:
						break;
					case 132:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -123:
						break;
					case 133:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -124:
						break;
					case 134:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -125:
						break;
					case 135:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -126:
						break;
					case 136:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -127:
						break;
					case 137:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -128:
						break;
					case 138:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -129:
						break;
					case 139:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -130:
						break;
					case 140:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -131:
						break;
					case 141:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -132:
						break;
					case 142:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -133:
						break;
					case 143:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -134:
						break;
					case 144:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -135:
						break;
					case 145:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -136:
						break;
					case 146:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -137:
						break;
					case 147:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -138:
						break;
					case 148:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -139:
						break;
					case 149:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -140:
						break;
					case 150:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -141:
						break;
					case 151:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -142:
						break;
					case 152:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -143:
						break;
					case 153:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -144:
						break;
					case 154:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -145:
						break;
					case 155:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -146:
						break;
					case 156:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -147:
						break;
					case 157:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -148:
						break;
					case 158:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -149:
						break;
					case 159:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -150:
						break;
					case 160:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -151:
						break;
					case 161:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -152:
						break;
					case 162:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -153:
						break;
					case 163:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -154:
						break;
					case 164:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -155:
						break;
					case 165:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -156:
						break;
					case 166:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -157:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
