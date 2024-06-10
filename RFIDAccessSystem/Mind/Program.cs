using System;
using System.Collections.Generic;
using System.Text;
using System.IO.Ports; //library za port funkcije
using System.Security.Cryptography; //library za SHA512
using MySql.Data.MySqlClient; //library za spajanje i slanje zahtjeva na bazu

namespace IDreadersender
{
    public class Check
    {
        Baza upis = new Baza();
        public int checkthis(Hash podaci, string plaintext, List<string> ids, List<string> ibs,List<string> imena, List<string> prezimena, List<string> salts, List<string> emails )
        {

            int brojilo = 0;
            foreach (string id in ids)
            {
                brojilo++;
                podaci.data = plaintext + salts[brojilo-1]; //slanje plaintexta
                podaci.Hashthis(); //zvanje metode za hash
                //Console.WriteLine(podaci.hashedData);
                //Console.WriteLine(id);
                if (id == podaci.hashedData)
                {
                    Console.WriteLine("Otvoreno za: " + imena[brojilo-1] + " " + prezimena[brojilo-1] + " IB: " + ibs[brojilo-1]);
                    upis.Log(ibs[brojilo - 1]);
                    brojilo = 0;
                    return 1;
                }
            }
            return 0;
        }

    }
    public class Baza
    {
        public List<string> ids = new List<string>();
        public List<string> ibs = new List<string>();
        public List<string> imena = new List<string>();
        public List<string> prezimena = new List<string>();
        public List<string> salts = new List<string>();
        public List<string> emails = new List<string>();
        public void Connect()
        {
            MySqlConnection dbCon;
            MySqlConnectionStringBuilder builder = new MySqlConnectionStringBuilder();

            builder.Server = "127.0.0.1";
            builder.Database = "rfid";
            builder.UserID = "root";
            builder.Password = "";
            String connString = builder.ToString();

            dbCon = new MySqlConnection(connString);

            String query = "SELECT * FROM osobe";
            MySqlCommand getID = new MySqlCommand(query, dbCon);
            dbCon.Open();
            Console.WriteLine("***************************************************************");
            Console.WriteLine("Uspiješno spojeno na Bazu podataka."/*  + connString*/);
            MySqlDataReader reader = getID.ExecuteReader();

            while (reader.Read())
            {
                ids.Add(reader["ID"].ToString());
                ibs.Add(reader["IB"].ToString());
                imena.Add(reader["Ime"].ToString());
                prezimena.Add(reader["Prezime"].ToString());
                salts.Add(reader["Salt"].ToString());
                emails.Add(reader["Email"].ToString());
            }
            dbCon.Close();
            Console.WriteLine("Odspojeno.");
        }
        public void Log(string ib)
        {
            MySqlConnection dbCon;
            MySqlConnectionStringBuilder builder = new MySqlConnectionStringBuilder();

            builder.Server = "127.0.0.1";
            builder.Database = "rfid";
            builder.UserID = "root";
            builder.Password = "";
            String connString = builder.ToString();

            dbCon = new MySqlConnection(connString);

            DateTime sada = DateTime.Now;
            String query = "INSERT INTO zapisnik (Vrijeme, IB) VALUES ('" + sada + "','" + ib + "')";
            MySqlCommand upis = new MySqlCommand(query, dbCon);
            dbCon.Open();

            Console.WriteLine("---------------------------------------------------------------");
            Console.WriteLine("Uspiješno spojeno na Bazu podataka."/*  + connString*/);
            MySqlDataReader reader = upis.ExecuteReader();

            while (reader.Read())
            {
            }
            dbCon.Close();
            Console.WriteLine("Odspojeno.");
        }
    }
    public class Hash
    {
        public string data; //plaintext hasha
        byte[] hashed; //hash text
        public string hashedData; //poljepsani hash text

        public void Hashthis()
        {
            SHA512 hashSvc = SHA512.Create(); //kreiranje SHA512

            hashed = hashSvc.ComputeHash(Encoding.UTF8.GetBytes(data)); //hashanje
            hashedData = BitConverter.ToString(hashed).Replace("-", ""); //uljepšavanje
        }
    }
        class Program
        {
            static SerialPort port; //inicijalizacija varijable
            static void Main(string[] args)
            {
                int status = 0;
                Hash podaci = new Hash(); //kreiranje objekta
                Baza sad = new Baza();
                Check checkid = new Check();

                port = new SerialPort
                {
                    PortName = "COM3", //definiranje imena porta
                    BaudRate = 9600 //definiranje BaudRatea
                };
                port.Open(); //otvaranje porta

                while (true)
                {
                    string temp = port.ReadLine();
                    if ((temp != "z\r") && (temp != "o\r") && (temp != "") && (temp != "\r"))
                    {
                        sad.Connect();
                        temp = temp.Replace(" ", "");
                        temp = temp.Replace("\r", "");//formatiranje
                        Console.WriteLine(DateTime.Now.ToString());
                        //podaci.data = temp; //slanje plaintexta
                        //podaci.Hashthis(); //zvanje metode za hash
                        status = checkid.checkthis(podaci, temp, sad.ids, sad.ibs, sad.imena, sad.prezimena, sad.salts, sad.emails);

                        if(status == 1)
                        {
                        port.Write("o");
                        }
                        else
                        {
                        port.Write("z");
                    }

                        //provjera
                        //Console.WriteLine(podaci.hashedData);
                        //Console.WriteLine(DateTime.Now.ToString() + " RFID ID: " + temp);

                        sad.ids.Clear();
                        sad.imena.Clear();
                        sad.prezimena.Clear();
                        sad.salts.Clear();
                }
                }
            }
        }
}