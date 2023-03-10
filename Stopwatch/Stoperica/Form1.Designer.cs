namespace Stoperica
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.ispis = new System.Windows.Forms.Label();
            this.start = new System.Windows.Forms.Button();
            this.backgroundWorker1 = new System.ComponentModel.BackgroundWorker();
            this.reset = new System.Windows.Forms.Button();
            this.split = new System.Windows.Forms.Button();
            this.splitIspis = new System.Windows.Forms.RichTextBox();
            this.save = new System.Windows.Forms.Button();
            this.saveime = new System.Windows.Forms.TextBox();
            this.ispisLoads = new System.Windows.Forms.ComboBox();
            this.load = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // ispis
            // 
            this.ispis.AutoSize = true;
            this.ispis.Font = new System.Drawing.Font("Microsoft Sans Serif", 26.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.ispis.Location = new System.Drawing.Point(35, 24);
            this.ispis.Name = "ispis";
            this.ispis.Size = new System.Drawing.Size(246, 39);
            this.ispis.TabIndex = 0;
            this.ispis.Text = "00:00:0000000";
            // 
            // start
            // 
            this.start.Location = new System.Drawing.Point(81, 81);
            this.start.Name = "start";
            this.start.Size = new System.Drawing.Size(85, 46);
            this.start.TabIndex = 1;
            this.start.Text = "Start";
            this.start.UseVisualStyleBackColor = true;
            this.start.Click += new System.EventHandler(this.start_Click);
            // 
            // backgroundWorker1
            // 
            this.backgroundWorker1.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker1_DoWork);
            // 
            // reset
            // 
            this.reset.Location = new System.Drawing.Point(172, 81);
            this.reset.Name = "reset";
            this.reset.Size = new System.Drawing.Size(85, 46);
            this.reset.TabIndex = 2;
            this.reset.Text = "Reset";
            this.reset.UseVisualStyleBackColor = true;
            this.reset.Click += new System.EventHandler(this.reset_Click);
            // 
            // split
            // 
            this.split.Location = new System.Drawing.Point(127, 133);
            this.split.Name = "split";
            this.split.Size = new System.Drawing.Size(85, 46);
            this.split.TabIndex = 3;
            this.split.Text = "Split";
            this.split.UseVisualStyleBackColor = true;
            this.split.Click += new System.EventHandler(this.split_Click);
            // 
            // splitIspis
            // 
            this.splitIspis.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.splitIspis.Location = new System.Drawing.Point(12, 201);
            this.splitIspis.Name = "splitIspis";
            this.splitIspis.ReadOnly = true;
            this.splitIspis.Size = new System.Drawing.Size(322, 225);
            this.splitIspis.TabIndex = 4;
            this.splitIspis.Text = "";
            // 
            // save
            // 
            this.save.Location = new System.Drawing.Point(12, 484);
            this.save.Name = "save";
            this.save.Size = new System.Drawing.Size(85, 46);
            this.save.TabIndex = 5;
            this.save.Text = "Save";
            this.save.UseVisualStyleBackColor = true;
            this.save.Click += new System.EventHandler(this.save_Click);
            // 
            // saveime
            // 
            this.saveime.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.saveime.Location = new System.Drawing.Point(12, 447);
            this.saveime.Name = "saveime";
            this.saveime.Size = new System.Drawing.Size(138, 26);
            this.saveime.TabIndex = 6;
            this.saveime.Text = "Name of the save";
            this.saveime.Enter += new System.EventHandler(this.saveime_Enter);
            this.saveime.Leave += new System.EventHandler(this.saveime_Leave);
            // 
            // ispisLoads
            // 
            this.ispisLoads.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.ispisLoads.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.ispisLoads.FormattingEnabled = true;
            this.ispisLoads.Location = new System.Drawing.Point(196, 447);
            this.ispisLoads.Name = "ispisLoads";
            this.ispisLoads.Size = new System.Drawing.Size(138, 28);
            this.ispisLoads.TabIndex = 7;
            this.ispisLoads.Click += new System.EventHandler(this.ispisLoads_Click);
            // 
            // load
            // 
            this.load.Location = new System.Drawing.Point(249, 484);
            this.load.Name = "load";
            this.load.Size = new System.Drawing.Size(85, 46);
            this.load.TabIndex = 8;
            this.load.Text = "Load";
            this.load.UseVisualStyleBackColor = true;
            this.load.Click += new System.EventHandler(this.load_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(346, 542);
            this.Controls.Add(this.load);
            this.Controls.Add(this.ispisLoads);
            this.Controls.Add(this.saveime);
            this.Controls.Add(this.save);
            this.Controls.Add(this.splitIspis);
            this.Controls.Add(this.split);
            this.Controls.Add(this.reset);
            this.Controls.Add(this.start);
            this.Controls.Add(this.ispis);
            this.Name = "Form1";
            this.Text = "Štoperica";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label ispis;
        private System.Windows.Forms.Button start;
        private System.ComponentModel.BackgroundWorker backgroundWorker1;
        private System.Windows.Forms.Button reset;
        private System.Windows.Forms.Button split;
        private System.Windows.Forms.RichTextBox splitIspis;
        private System.Windows.Forms.Button save;
        private System.Windows.Forms.TextBox saveime;
        private System.Windows.Forms.ComboBox ispisLoads;
        private System.Windows.Forms.Button load;
    }
}

