from flask import Flask, render_template, request
import sqlite3 as sql, string,random
from datetime import datetime
import hashlib
app = Flask(__name__)
app.secret_key = 'Qef4s789BBcV78PollMnb'

def get_cursor():
    conn = sql.connect('ledger.db')
    return conn.cursor()

def generate_random_id(length=10):
    characters = string.ascii_letters + string.digits
    return ''.join(random.choice(characters) for _ in range(length))

@app.route('/', methods=['GET', 'POST'])
def input_complaint():
    website = 'Ledger.html'
    fir_id=''
    if request.method == 'POST':
        uid=request.form['uid']
        fname = request.form['fname']
        lname = request.form['lname']
        age = request.form['age']
        resaddress = request.form['resaddress']
        cnumber = request.form['cnumber']
        date_time=request.form['date_time']
        date_time=datetime.strptime(date_time, '%Y-%m-%dT%H:%M')
        locincident = request.form['locincident']
        tyincident = request.form['tyincident']
        descincident = request.form['descincident']
        sufferings = request.form['sufferings']
        perpdesc = request.form['perpdesc']
        perpnick = request.form['perpnick']
        crimebrief=request.form['crimebrief']
        fir_id=generate_random_id()
        unique_id=hashlib.sha256(fir_id.encode()).hexdigest()

        cur = get_cursor()
        cur.execute('''create table if not exists ledger_db(
                    fir_id VARCHAR unique,
                    uid VARCHAR(7),
                    a VARCHAR(30),b VARCHAR(30),c VARCHAR(10),
                    d INTEGER,e VARCHAR(50),f VARCHAR(30),
                    g VARCHAR(500),h VARCHAR(200),
                    i VARCHAR(300),j VARCHAR(300),
                    k VARCHAR(100),l VARCHAR(100),
                    m VARCHAR(100)
        )''')
        cur.execute('''
        insert into ledger_db values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        ''',(unique_id,uid,fname, lname, cnumber, age, resaddress,date_time, locincident, tyincident, descincident, sufferings, perpdesc, perpnick,crimebrief))
        print("Insert success!")
        cur.connection.commit()


        cur.execute('''select * from ledger_db''')
        result=cur.fetchall()
        print("Fetch Success!")
        print("THIS IS A TEST OF INSERTION!")
        for row in result:
            print(row)
        return render_template(website,message="FIR Successfully Filed. Please Copy and Remember: ",fir_id=fir_id)
    else:
        return render_template(website)

@app.route('/fir_search',methods=['GET','POST'])
def search_complaint():
    website='Search_Ledger.html'
    if request.method=='POST':
        uid=request.form['uid']
        fir_id=request.form['fir_id']
        fir_id=hashlib.sha256(fir_id.encode()).hexdigest()
        cnumber=request.form['cnumber']
#(fname, lname, cnumber, age, resaddress,date_time, locincident, tyincident, descincident, sufferings, perpdesc, perpnick,crimebrief)
        cur=get_cursor()
        cur.execute('''
                    select a,b,c,e,f,g,h,i,j from ledger_db where
                    uid=? and fir_id=? and c=?
                    ''',(uid,fir_id,cnumber))
        result=cur.fetchall()
        ashwin=[]
        if result:
            # res1,res2,res3,res4,res5=result[4],result[5],result[6],result[7],result[8]
            for item in result[0]:
                ashwin.append(item)
            c_name="Namaste...🙏  "+ashwin[0]+" "+ashwin[1]+","
            c_info="Information:  📞"+ashwin[2]+",  🏠"+ashwin[3]
            date_of_crime=ashwin[4]
            location_of_crime=ashwin[5]
            type_of_crime=ashwin[6]
            desc_of_crime=ashwin[7]
            sufferings=ashwin[8]
            return render_template(website,a1="Registered Date",a2="Location",a3="Crime Category",a4="Description",a5="Victimization",c_name=c_name,c_info=c_info,a=date_of_crime,b=location_of_crime,c=type_of_crime,d=desc_of_crime,e=sufferings)
        else:
            print("No result Found!")
            return render_template(website,warning="Record NOT Found! Kindly check your credentials!")
    else:return render_template(website)
    
if __name__=='__main__':
    app.run(debug=True)
