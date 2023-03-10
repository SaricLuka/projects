using System;
using System.Drawing;
using System.Windows.Forms;

namespace Kalkulator
{
    public partial class Form1 : Form
    {
        float broj = 0;
        float temp = 0;
        int flag;
        string ispis;
        bool b = true;
        public Form1()
        {
            InitializeComponent();
            //this.BackColor = Color.Silver;
            //this.TransparencyKey = Color.Silver;
            upis.Text = Convert.ToString(broj);

        }
        public void Ispis(int x)
        {
            ispis = upis.Text + x;
            upis.Text = ispis;
            broj = float.Parse(ispis);
            if (b == true)
            {
                upis.Text = Convert.ToString(broj);
            }
            b = false;
        }

        public void Operacija(int op)
        {
            if (flag != 0)
            {
                bIzracun.PerformClick();
            }
            temp = float.Parse(upis.Text);
            if (op == 1)
            {
                ispis = upis.Text + "÷";
                flag = op;
            }
            else if (op == 2)
            {
                ispis = upis.Text + "×";
                flag = op;
            }
            else if (op == 3)
            {
                ispis = upis.Text + "-";
                flag = op;
            }
            else if (op == 4)
            {
                ispis = upis.Text + "+";
                flag = op;
            }
            upis.Text = "";
            ispis2.Text = ispis;
        }
        public void Brisi()
        {
            ispis = upis.Text;
            ispis = ispis.Substring(0, ispis.Length - 1);
            upis.Text = ispis;
            broj = float.Parse(ispis);
        }

        private void upis_TextChanged(object sender, EventArgs e)
        {

        }

        private void b0_Click(object sender, EventArgs e)
        {
            Ispis(0);
        }

        private void b1_Click(object sender, EventArgs e)
        {
            Ispis(1);
        }

        private void b2_Click(object sender, EventArgs e)
        {
            Ispis(2);
        }

        private void b3_Click(object sender, EventArgs e)
        {
            Ispis(3);
        }

        private void b4_Click(object sender, EventArgs e)
        {
            Ispis(4);
        }

        private void b5_Click(object sender, EventArgs e)
        {
            Ispis(5);
        }

        private void b6_Click(object sender, EventArgs e)
        {
            Ispis(6);
        }

        private void b7_Click(object sender, EventArgs e)
        {
            Ispis(7);
        }

        private void b8_Click(object sender, EventArgs e)
        {
            Ispis(8);
        }

        private void b9_Click(object sender, EventArgs e)
        {
            Ispis(9);
        }

        private void bTocka_Click(object sender, EventArgs e)
        {
            ispis = upis.Text + ".";
            upis.Text = ispis;
        }

        private void bDijeli_Click(object sender, EventArgs e)
        {
            Operacija(1);
        }

        private void bMnozi_Click(object sender, EventArgs e)
        {
            Operacija(2);
        }

        private void bOduzmi_Click(object sender, EventArgs e)
        {
            Operacija(3);
        }

        private void bZbroji_Click(object sender, EventArgs e)
        {
            Operacija(4);
        }

        private void bIzracun_Click(object sender, EventArgs e)
        {
            if (flag==1)
            {
                ispis2.Text = ispis2.Text + Convert.ToString(broj) + "=";
                broj = temp / broj;
                upis.Text=Convert.ToString(broj);
            }
            else if (flag == 2)
            {
                ispis2.Text = ispis2.Text + Convert.ToString(broj) + "=";
                broj = temp * broj;
                upis.Text = Convert.ToString(broj);
            }
            else if (flag == 3)
            {
                ispis2.Text = ispis2.Text + Convert.ToString(broj) + "=";
                broj = temp - broj;
                upis.Text = Convert.ToString(broj);
            }
            else if (flag == 4)
            {
                ispis2.Text = ispis2.Text + Convert.ToString(broj) + "=";
                broj = temp + broj;
                upis.Text = Convert.ToString(broj);
            }
            flag = 0;
        }

        private void Form1_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.D0 || e.KeyCode == Keys.NumPad0)
            {
                b0.PerformClick();
            }
            else if (e.KeyCode == Keys.D1 || e.KeyCode == Keys.NumPad1)
            {
                b1.PerformClick();
            }
            else if (e.KeyCode == Keys.D2 || e.KeyCode == Keys.NumPad2)
            {
                b2.PerformClick();
            }
            else if (e.KeyCode == Keys.D3 || e.KeyCode == Keys.NumPad3)
            {
                b3.PerformClick();
            }
            else if (e.KeyCode == Keys.D4 || e.KeyCode == Keys.NumPad4)
            {
                b4.PerformClick();
            }
            else if (e.KeyCode == Keys.D5 || e.KeyCode == Keys.NumPad5)
            {
                b5.PerformClick();
            }
            else if (e.KeyCode == Keys.D6 || e.KeyCode == Keys.NumPad6)
            {
                b6.PerformClick();
            }
            else if (e.KeyCode == Keys.D7 || e.KeyCode == Keys.NumPad7)
            {
                b7.PerformClick();
            }
            else if (e.KeyCode == Keys.D8 || e.KeyCode == Keys.NumPad8)
            {
                b8.PerformClick();
            }
            else if (e.KeyCode == Keys.D9 || e.KeyCode == Keys.NumPad9)
            {
                b9.PerformClick();
            }
            else if (e.KeyCode == Keys.Divide)
            {
                bDijeli.PerformClick();
            }
            else if (e.KeyCode == Keys.Multiply)
            {
                bMnozi.PerformClick();
            }
            else if (e.KeyCode == Keys.Subtract)
            {
                bOduzmi.PerformClick();
            }
            else if (e.KeyCode == Keys.Add)
            {
                bZbroji.PerformClick();
            }
            else if (e.KeyCode == Keys.Decimal)
            {
                bTocka.PerformClick();
            }
            else if (e.KeyCode == Keys.Enter)
            {
                bIzracun.PerformClick();
            }
            else if (e.KeyCode == Keys.Back)
            {
                Brisi();
            }
        }
    }
}