using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Stoperica
{
    public partial class Form1 : Form
    {
        bool status = false;
        int brojilo = 1;
        int i = 0;
        TimeSpan razlika;
        TimeSpan temp;
        TimeSpan tempSplit = TimeSpan.Zero;

        string path = @"C:\Users\" + Environment.UserName + @"\stopericadata.txt";
        List<string> stringLoads = new List<string>();
        public Form1()
        {
            InitializeComponent();

        }

        public void Zapis()
        {
            stringLoads = File.ReadAllLines(path).ToList();
            ispisLoads.Items.Clear();
            int tempindex;
            string tempime = "\t\n";
            for (i = 0; i < stringLoads.Count; i++)
            {
                tempindex = stringLoads[i].IndexOf("\t");
                tempime = stringLoads[i].Substring(0, tempindex);
                tempime = tempime.Replace("\t", "");

                ispisLoads.Items.Add(tempime);
            }
        }
        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            backgroundWorker1.WorkerSupportsCancellation = true;
            DateTime pocetno = DateTime.Now;
            DateTime trenutacno;
            while (!backgroundWorker1.CancellationPending)
            {
                trenutacno = DateTime.Now;
                razlika = trenutacno - pocetno;
                if (!(temp == null))
                {
                    razlika = temp + razlika;
                }
                ispis.Text = razlika.ToString();
            }
        }
        private void start_Click(object sender, EventArgs e)
        {
            if (status == false)
            {
                backgroundWorker1.RunWorkerAsync();
                start.Text = "Stop";
                status = true;
            }
            else
            {
                backgroundWorker1.CancelAsync();
                temp = razlika;
                start.Text = "Start";
                status = false;
            }
        }

        private void split_Click(object sender, EventArgs e)
        {
            tempSplit = razlika - tempSplit;
            splitIspis.Text = splitIspis.Text + "Split #" + brojilo + " Time: " + razlika + " Diff: " + tempSplit + "\n";
            tempSplit = razlika;
            brojilo++;
        }

        private void reset_Click(object sender, EventArgs e)
        {
            backgroundWorker1.CancelAsync();
            ispis.Text = "00:00:0000000";
            splitIspis.Text = "";
            tempSplit = TimeSpan.Zero;
            temp = TimeSpan.Zero;
            start.Text = "Start";
            status = false;
        }

        private void save_Click(object sender, EventArgs e)
        {
            if (saveime.Text != "Name of the save") {
                string tempupis = saveime.Text + "\t" + ispis.Text + "\t" + splitIspis.Text.Replace("\n", "\t") + tempSplit + "\n";
                File.AppendAllText(path, tempupis);
            }
        }

        private void saveime_Enter(object sender, EventArgs e)
        {
            if (saveime.Text == "Name of the save")
            {
                saveime.Text = "";
            }
        }

        private void saveime_Leave(object sender, EventArgs e)
        {
            if (saveime.Text == "")
            {
                saveime.Text = "Name of the save";
            }
        }

        private void ispisLoads_Click(object sender, EventArgs e)
        {
            Zapis();
        }

        private void load_Click(object sender, EventArgs e)
        {
            string izabrano = ispisLoads.Text;
            string saved;
            string ime;
            string splits;
            int index;
            int count;
            TimeSpan vrijeme;
            TimeSpan splitvrijeme;
            for (i = 0; i < stringLoads.Count; i++)
            {
                if (stringLoads[i].Contains(izabrano))
                {
                    saved = stringLoads[i];
                    index = saved.IndexOf("\t");
                    ime = saved.Substring(0, index);
                    ime = ime.Replace("\t", "");
                    saved = saved.Replace(ime + "\t", "");


                    index = saved.IndexOf("\t");
                    vrijeme = TimeSpan.Parse(saved.Substring(0, index));
                    temp = vrijeme;
                    razlika = vrijeme;
                    ispis.Text = vrijeme.ToString();
                    saved = saved.Replace(Convert.ToString(vrijeme) + "\t", "");


                    count = saved.Count(f => (f == 't'));
                    brojilo = count + 1;

                    index = saved.LastIndexOf("\t");
                    splits = saved.Substring(0, index);
                    saved = saved.Replace(splits, "");
                    splits = splits.Replace("\n", "");
                    splits = splits.Replace("\t", "\n");
                    splitIspis.Text = splits + "\n";

                    saved = saved.Replace("\n", "");
                    saved = saved.Replace("\t", "");
                    splitvrijeme = TimeSpan.Parse(saved.Substring(0, saved.Length));
                    tempSplit = splitvrijeme;
                }
            }
        }
    }
}
