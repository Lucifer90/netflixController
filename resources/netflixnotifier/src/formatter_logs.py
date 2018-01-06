# coding=utf-8
import datetime
import os


def set_underscore_position(name):
    """
    Imposta la posizione dell'underscore che indica l'eventuale ripetizione di un file di log.
    Scorre la stringa al contrario, se incontra un - PRIMA di un _ significa che non ci sono state ripetizioni di nome.
    :param name: nome candidato.
    :return: ritorna la posizione dell'eventuale underscore
    """
    idx = 0
    for c in name[::-1]:
        idx += 1
        if c == '-':
            return 0
        elif c == '_':
            result = len(name)-idx
            return result


def set_logger_file(path, name, log_size=5000000):
    """
    Metodo che crea la stringa del file di log da utilizzare
    :param path: percorso dove si vogliono salvare i file di log
    :param name: nome del file di log che si vuole creare, della forma: <nome_log>%s.<estensione>
    :param log_size: grandezza massima dell'ultimo log al quale aggiungere testo
    :return: ritorna la stringa del file di log
    """
    if not os.path.exists(path):
        os.makedirs(path)
    extension = "."+name.split(".")[1]
    if path[-1] != "/":
        path += "/"
    candidate = name % (datetime.datetime.today().strftime("%Y-%m-%d"))
    files = []
    for i in os.listdir(path):
        if os.path.isfile(os.path.join(path, i)) and candidate[:-4] in i and extension in i:
            files.append(i)
    return get_similar_log_name(files, candidate, path, log_size)


def get_similar_log_name(files, candidate, path, log_size):
    """
    Crea la stringa vera e propria. in caso di altri file con stessa data controlla se sono già più grandi di 5 mega,
    nel caso crea un ulteriore file con un contatore nel nome, altrimenti aggiunge informazioni nello stesso log.
    :param files: lista file con radice filename comune
    :param candidate: stringa candidata, inizialmente si ipotizza che non ci siano altri file di log con stessa data
    :param path: percorso cartella log
    :param log_size: grandezza del file log
    :return: ritorna
    """
    if not files:
        return candidate
    file = get_recent_candidate(files, path)
    underscore_pos = set_underscore_position(file)
    if os.path.getsize(os.path.join(path, file)) < log_size:
        return file
    elif not file[underscore_pos] == "_":
        string = candidate[:-4]+"_1"+candidate[-4:]
    else:
        string = file[:underscore_pos+1]+str(int(file[underscore_pos+1:-4])+1)+file[-4:]
    return string


def get_recent_candidate(files, path):
    """
    Scorre i file di log datati nello stesso giorno cercando il più recente
    :param files: lista file con radice simile al candidato nel nome
    :param path: percorso output file di log
    :return: ritorna il file di log più recente
    """
    data = os.path.getmtime(path+files[0])
    filet = files[0]  # sembra non essere necessario
    for i in files:
        if os.path.getmtime(path+i) >= data:
            data = os.path.getmtime(path+i)
            filet = i
    return filet


if __name__ == "__main__":
    """
    Test
    """
    print(set_logger_file("/tmp", "prova %s ok.txt"))