import sys

import mainCaller

if __name__ == "__main__":
    arg = sys.argv[1]
    mainCaller.print_lista_utenti()
    mainCaller.add_manual_value(arg)
    mainCaller.print_lista_utenti()
