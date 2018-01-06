# coding=utf-8
import logging
import requests
import sys
from bs4 import BeautifulSoup
import datetime
import smtplib
import os
import formatter_logs

ROOT_DIR = os.path.normpath(os.getcwd() + os.sep + os.pardir)
ROOT_DIR = os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir)
LOG_BASE = os.path.join(ROOT_DIR, "logs")
RES_PATH = os.path.join(ROOT_DIR, "resources")

today = datetime.date.today()


mail_sender = "netflix.arezzo@gmail.com"
password_sender = "BraskaVSTheWorld"

minimum_limit = float(13.99)

user = "netflix.arezzo@gmail.com"
passwd = "BraskaVSTheWorld"

path_file = os.path.join(RES_PATH, "usersDB")
path_warning = os.path.join(RES_PATH, "warning_levelDB")
path_old_amount = os.path.join(RES_PATH, "old_amount")


error_mail_subject = "Errore NetflixNotifier"
to_pay_mail_subject = "Qui c'è da pagare"
under_limit_subject = "La soglia di netflix è scesa al di sotto del costo del premium."
expire_subject = "L'account Netflix è stato sospeso"

error_mail_text = "Si è presentato un errore"
to_pay_mail_text = "Toccherebbe a {0} pagare a sto giro. L'account scadrà il {1}"
under_limit_text = "Toccherà a {0} pagare a sto giro. L'account scadrà il {1}"
expire_text = "Toccava a {0} pagare a sto giro."
more_money_text = "Cifra versata: {0}\nNuovo ammontare: {1}"
less_money_text = "Cifra prelevata: {0}\nNuovo ammontare {1}"

netflix_tech_error = "Stiamo riscontrando difficolt"
netflix_tech_warning = "Problema temporaneo di Netflix"
wrong_login_data = "The login information you entered does not match an account in our records"
wrong_login_warning = "Dati di login errati"
more_money_subject = "Netflix: Nel tuo conto sono stati versati dei fondi"
less_money_subject = "Netflix: Sono stati prelevati dei fondi dal tuo conto"

month_converter = {
    "Gennaio": "January",
    "Febbraio": "February",
    "Marzo": "March",
    "Aprile": "April",
    "Maggio": "May",
    "Giugno": "June",
    "Luglio": "July",
    "Agosto": "August",
    "Settembre": "September",
    "Ottobre": "October",
    "Novembre": "November",
    "Dicembre": "December",
    "gennaio": "January",
    "febbraio": "February",
    "marzo": "March",
    "aprile": "April",
    "maggio": "May",
    "giugno": "June",
    "luglio": "July",
    "agosto": "August",
    "settembre": "September",
    "ottobre": "October",
    "novembre": "November",
    "dicembre": "December"
}

warning_level = {
    "warning_one": "Credito residuo sufficente",
    "warning_two": "Credito residuo sotto la soglia",
    "warning_three": "Credito residuo azzerato",
    "warning_four": "Account sospeso"
}


def cracker():
    s = requests.session()
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0',
        'Accept': 'application/json, text/javascript, */*; q=0.01',
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest'
    }

    account_page = s.get("https://www.netflix.com/YourAccount")
    account_content = account_page.content

    if "login_error_input_password" in account_content:
        url = "https://www.netflix.com/in/Login"
        try:
            fetch = s.get(url,headers=headers)
        except Exception as ex:
            logging.info("Errore durante connessione {0}".format(str(ex)))
            quit()
        bs = BeautifulSoup(fetch.content,"html.parser")
        list1 = bs.find_all("input",{"name":"authURL"})
        authval = list1[0]["value"]
        payload = {"email": user, "password": passwd, "rememberMe": "true", "flow": "websiteSignUp", "mode": "login",
                   "action": "loginAction", "withFields": "email,password,rememberMe,nextPage",
                   "authURL": authval, "nextPage": ""}
        try:
            login_page = s.post("https://www.netflix.com/in/Login", data=payload, headers=headers)
        except Exception as ex:
            logging.info("Errore durante la procedura di login {0}".format(str(ex)))
            quit()
        login_content = login_page.content

        if login_page.url == url:
            logging.info("Errore durante la procedura di login, invio mail all'admin")
            error_managment(login_content)
            quit()

        try:
            account_page = s.get("https://www.netflix.com/YourAccount")
        except Exception as ex:
            logging.info("Errore durante recupero pagina account {0}".format(str(ex)))
        account_content = account_page.content

    try:
        billing_activity = s.get("https://www.netflix.com/BillingActivity")
    except Exception as ex:
        logging.info("Errore durante recupero pagina fatturazione {0}".format(str(ex)))
    billing_content = billing_activity.content

    amount = get_amount(account_content)
    if amount:
        endingDate = get_billing_end_date(billing_content)
    else:
        amount = 0
        endingDate = get_billing_end_date(billing_content)
    return amount, endingDate


def error_managment(html):
    if netflix_tech_error in html:
        subj = netflix_tech_warning
        text = ""
    elif wrong_login_data in html:
        subj = wrong_login_warning
        text = ""
    else:
        return
    sendmail(luca_mail, subj, text)


def setup_data():
    warning = read_warning_level()
    logging.info("Livello di warning su {0}: {1}".format(warning, warning_level[warning]))
    amount, expire_date = cracker()
    if not amount and not expire_date:
        logging.info("Errore di computazone durante la lettura dei dati da server")
        quit()
    logging.info("Credito:{0} Data di scadenza: {1}".format(amount, expire_date))
    return warning, amount, expire_date, lista_utenti


def start_control():
    old_amount = get_old_amount()
    warning, amount, expire_date, lista_utenti = setup_data()
    if amount > minimum_limit and warning != "warning_one":
        set_warning_level("warning_one")
        lista_utenti[0][2] += 1
        logging.info("{0} ha recentemente pagato, incremento il numero dei suoi pagamenti.")
        logging.info("Credito:{0} Data di scadenza: {1}".format(amount, expire_date))
        write_on_usersDB(lista_utenti)
    elif amount < minimum_limit and warning == "warning_one":
        logging.info("Credito al di sotto del prezzo dell'account. Setto il warning su 2.")
        set_warning_level("warning_two")
        sendmail((lista_utenti[0][1], lista_utenti[1][1], lista_utenti[2][1]), under_limit_subject, under_limit_text,
                 str(expire_date))
        logging.info("Email inviate con successo")
    elif warning == "warning_two" and not amount:
        logging.info("Credito pari a 0. Setto il warning su 3.")
        set_warning_level("warning_three")
        sendmail((lista_utenti[0][1], lista_utenti[1][1], lista_utenti[2][1]), to_pay_mail_subject, to_pay_mail_text,
                 str(expire_date))
        logging.info("Email inviate con successo")
    elif warning == "warning_three" and not expire_date:
        logging.info("Account sospeso.")
        set_warning_level("warning_four")
        sendmail((lista_utenti[0][1], lista_utenti[1][1], lista_utenti[2][1]), expire_subject, expire_text)
        logging.info("Email inviate con successo.")


def get_old_amount():
    f = open(path_old_amount)
    value = float(f.read())
    f.close()
    return value


def write_old_amount(value):
    f = open(path_old_amount, "w")
    f.write(str(value))
    f.close()
    return


def check_amount(new_amount, old_amount):
    if new_amount > old_amount:
        subject = more_money_subject
        msg = more_money_text.format(str(new_amount-old_amount), str(new_amount))
    elif new_amount < old_amount:
        subject = less_money_subject
        msg = less_money_text.format(str(new_amount-old_amount), str(new_amount))
    else:
        return
    sendmail(mails, subject, msg)
    write_old_amount(new_amount)


def write_on_usersDB(lista_utenti):
    output = ""
    for element in lista_utenti:
        output += "{0},{1},{2}\n".format(element[0], element[1], element[2])
    f = open(path_file)
    f.write(output)
    f.close()


def get_key(tuple):
    return tuple[2]


def turn():
    lista = []
    f = open(path_file)
    line = f.readline()
    while line:
        user, mail, payments = line.split(",")
        payments = int(payments.replace("\n", ""))
        lista.append((user,mail,payments))
        line = f.readline()
    lista = sorted(lista, key=get_key)
    f.close()
    return lista


lista_utenti = turn()
luca_mail = ""
riccardo_mail = ""
ruben_mail = ""

for element in lista_utenti:
    if 'Luca' in element[0]:
        luca_mail = element[1]
    elif 'Ruben' in element[0]:
        ruben_mail = element[1]
    elif 'Riccardo' in element[0]:
        riccardo_mail = element[1]

mails = (luca_mail, ruben_mail, riccardo_mail)


def get_amount(html):
    if "Il tuo credito di" in html:
        response = html.split("amountRemaining\":")[1][:4]
        amount = response[:2]+"."+response[2:]
        return amount


def get_billing_end_date(html):
    if "Data della prossima fattura" in html:
        stepone = html.split("\"billingEndDate\":\"")[1]
        steptwo = stepone.split("\",")[0]
        stepthree = steptwo.split("\\x20")
        day = stepthree[0]
        month = stepthree[1]
        year = stepthree[2]
        scadenza = datetime.datetime.strptime(""+str(day)+" "+str(month_converter[month])+" "+str(year), "%d %B %Y")
        return scadenza



def sendmail(lista_destinatari, subject, body, expire_date=""):
    colpevole = lista_destinatari[0][0]
    if len(lista_destinatari) == 3:
        for destinatario in lista_destinatari:
            send_mail(destinatario, subject, body, colpevole, expire_date)
    else:
        send_mail(lista_destinatari, subject, body)


def send_mail(destinatario, subject, body, colpevole="", expire_date=""):
    try:
        SMTPServer = smtplib.SMTP_SSL()
        SMTPServer.connect('smtp.gmail.com', 465)
        SMTPServer.ehlo()
        try:
            SMTPServer.starttls()
        except:
            pass
        SMTPServer.login(mail_sender, password_sender)
        SMTPServer.sendmail(mail_sender, destinatario, message_creator(destinatario, subject, body, colpevole,
                                                                       expire_date))
        SMTPServer.quit()
        print "Successfully sent email"
    except smtplib.SMTPException as ex:
        print "Error: unable to send email {0}".format(str(ex))


def message_creator(destinatari, subject, body, colpevole, expire_date):
    if subject == expire_text:
        body = body.format(colpevole)
    elif subject == under_limit_subject or subject == to_pay_mail_subject:
        body = body.format(colpevole, str(expire_date))
    msg = "\r\n".join([
        "From: {0}".format("Account Netflix"),
        "To: {0}".format(destinatari),
        "Subject: {0}".format(subject),
        "",
        "{0}".format(body.format())
    ])
    return msg


def read_warning_level():
    f = open(path_warning)
    values = f.read().split(",")
    warning = values[0].replace("\n", "")
    for messaggio in warning_level:
        if warning == warning_level[messaggio]:
            f.close()
            return messaggio


def set_warning_level(selector):
    output = warning_level[selector]
    f = open(path_warning, "w")
    f.write(output)


def setting_up_log():
    LOG_SIZE = 5000000
    log_format = '%(asctime)s - %(levelname)s - %(filename)s:%(lineno)s - %(funcName)10s()- %(message)s'

    try:
        loggerfilename = formatter_logs.set_logger_file(LOG_BASE, "NetflixNotifier_%s.log", LOG_SIZE)
        logging.basicConfig(
            filename=os.path.join(LOG_BASE, loggerfilename),
            format=log_format, level=logging.INFO)
    except:
        loggerfilename = formatter_logs.set_logger_file("/tmp", "NetflixNotifier_%s.log", LOG_SIZE)
        logging.basicConfig(
            filename=os.path.join("/tmp", loggerfilename),
            format=log_format, level=logging.INFO)
    logging.getLogger("requests").setLevel(logging.INFO)


def add_manual_value(nomeutente, user_list=""):
    if user_list == "":
        user_list = turn()
    nomeutente = nomeutente.lower()
    if nomeutente == "" or nomeutente not in ("luca", "ruben", "riccardo"):
        logging.error("Usernamene non valida")
        quit()
    output_list = []
    for elemento in user_list:
        if nomeutente == elemento[0].lower():
            output_list.append((elemento[0], elemento[1], str(int(elemento[2])+1)))
        else:
            output_list.append(element)
    write_on_usersDB(output_list)


def get_lista_utenti():
    return lista_utenti


def print_lista_utenti():
    print get_lista_utenti()


def welcome_email():
    sendmail((mails), "Email di benvenuto", "Questa è una mail di benvenuto. Da ora in poi "
                                                                      "verrete notificati ogni volta che il vostro "
                                                                      "account Netflix avrà bisogno di attenzioni, "
                                                                      "ovvero in carenza di credito o in scadenza di "
                                                                      "contratto."
                                                                      "\n-Sempre che sto programmino funzioni come "
                                                                      "previsto.\n\n"
                                                                      "Luca Fanciullini")


if __name__ == "__main__":
    firstLaunch = os.path.join(os.path.join(ROOT_DIR, "resources"), "firstLaunch")
    if os.path.exists(firstLaunch):
        welcome_email()
        os.rename(firstLaunch, os.path.join(RES_PATH, "_firstLaunch"))
    setting_up_log()
    logging.getLogger("requests").setLevel(logging.WARNING)
    logging.info("Inizio controllo")
    start_control()
