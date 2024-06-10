using System;
using System.Windows.Forms;
using System.IO.Ports; //library za port funkcije
using System.Security.Cryptography; //library za SHA512
using System.Text;
using System.ComponentModel;
using MySql.Data.MySqlClient;
using System.Drawing;

namespace Converter
{

    public partial class Form1 : Form
    {
        Hash podaci = new Hash(); //kreiranje objekta
        string salt;
        public Form1()
        {
            InitializeComponent();
            backgroundWorker1.RunWorkerAsync();
            ispis.ForeColor = Color.Blue;
            ispis.Text = "Skenirjate karticu."; 
        }
        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            SerialPort port; //inicijalizacija varijable

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
                    temp = temp.Replace(" ", "");
                    temp = temp.Replace("\r", "");//formatiranje
                    salt = GetSalt();
                    podaci.data = temp + salt; //slanje plaintexta
                    podaci.Hashthis(); //zvanje metode za hash
                    ispis.ForeColor = Color.Green;
                    ispis.Text = "Generiran Hash i Salt. Nakon upisa ostalih informacija, možete kliknuti upis.";
                }
            }
        }

        private void reset_Click(object sender, EventArgs e)
        {
            podaci.hashedData = "";
            salt = "";
            ime.Text = "";
            prezime.Text = "";
            email.Text = "";
            ispis.ForeColor = Color.Blue;
            ispis.Text = "Skenirjate karticu.";
        }
        private static string GetSalt()
        {
            var random = new RNGCryptoServiceProvider();
            byte[] salt = new byte[24];
            random.GetBytes(salt);
            return Convert.ToBase64String(salt);
        }

        private void upis_Click(object sender, EventArgs e)
        {
            if ((ime.Text == "") || (prezime.Text == "") || (email.Text == "") || (podaci.hashedData == "") || (salt == "")) {
                reset.PerformClick();
                ispis.ForeColor = Color.Red;
                ispis.Text = "Neka polja su prazna. Proces resetiran. Započnite ispočetka.";
            }
            else
            {
                UpisBaza();
            }
        }

        void UpisBaza()
        {
                MySqlConnection dbCon;
                MySqlConnectionStringBuilder builder = new MySqlConnectionStringBuilder();

                builder.Server = "127.0.0.1";
                builder.Database = "rfid";
                builder.UserID = "root";
                builder.Password = "";
                String connString = builder.ToString();

                dbCon = new MySqlConnection(connString);
                
                String query = "INSERT INTO osobe (Ime, Prezime, Email, ID, Salt) VALUES ('" + ime.Text + "','" + prezime.Text + "','" + email.Text + "','" + podaci.hashedData + "','" + salt + "')";
                MySqlCommand upis = new MySqlCommand(query, dbCon);
                dbCon.Open();

                MySqlDataReader reader = upis.ExecuteReader();

                while (reader.Read())
                {
                }
                dbCon.Close();
                ispis.ForeColor = Color.Green;
                ispis.Text = "Upis obavljen!";
        }


    }
    public class Hash
    {
        public string data; //plaintext hasha
        byte[] hashed; //hash text
        public string hashedData; //poljepsani hash text

        public string Hashthis()
        {
            SHA512 hashSvc = SHA512.Create(); //kreiranje SHA512

            hashed = hashSvc.ComputeHash(Encoding.UTF8.GetBytes(data)); //hashanje
            hashedData = BitConverter.ToString(hashed).Replace("-", ""); //uljepšavanje
            return hashedData;
        }
    }

}
