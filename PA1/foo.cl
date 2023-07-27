class Main {
    io : IO <- new IO;
    operacion : String;
    firstNumber : String;
    secondNumber : String;
    symbol : String;
    a2i : A2I <- new A2I;

    main() : Object {
        {
            operacion <- io.in_string();
            let firstBlank : Int <- 0,
                secondBlank : Int <- 0 in {
                firstBlank <- findBlank(0, operacion);
                secondBlank <- findBlank(firstBlank + 1, operacion);
                firstNumber <- operacion.substr(0, firstBlank);
                symbol <- operacion.substr(firstBlank + 1, 1);
                secondNumber <- operacion.substr(secondBlank + 1, operacion.length() - secondBlank - 1);
            };
            let number1 : Int,
                number2 : Int in {
                number1 <- a2i.a2i(firstNumber);
                number2 <- a2i.a2i(secondNumber);
                if symbol = "+" then
                    io.out_int(number1 + number2)
                else if symbol = "-" then
                    io.out_int(number1 - number2)
                else if symbol = "*" then
                    io.out_int(number1 * number2)
                else if symbol = "/" then
                    io.out_int(number1 / number2)
                else
                    io.out_string("El symbolo de operacion no es valido.")
                fi fi fi fi;
                io.out_string("Nuevo string \
                nuevo intento");
                io.out_string("\n");
            };
        }
    };

    findBlank(start : Int, text : String) : Int {
        {
            let i : Int <- start,
                len : Int <- text.length(),
                space : Int <- 0 in {
                while i < len loop {
                    if text.substr(i, 1) = " " then {
                        if space = 0 then space <- i else 1 fi;
                    } else 1 fi;
                    i <- i + 1;
                } pool;
                space;
            };
        }
    };
};