package com.dachen.mediecinelibraryrealize.entity;

import java.io.Serializable;
import java.util.List;

import com.dachen.medicine.entity.Result;

public class Patients extends Result implements Serializable{
	private static final long serialVersionUID = -1290246555047525534L;
	//{"c_user_patient_list":[{"id":1228 ,"user_name":"11","num_recipes":3 ,"num_remind":0 ,"reminds":[]}]}


	public List<patient> c_user_patient_list;
	public Patients(){
		try {
			super.deepCopy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String toString() {
		return "Patients [c_user_patient_list=" + c_user_patient_list + "]";
	}

	public class patient extends Result implements Serializable{


		private static final long serialVersionUID = -2285052158340533509L;
		public String user_name;
		public String id;
		public int num_recipes;
		public int num_remind;
		public String logo;
		public List<Reminds> reminds;
		public int num_dhs;
		public patient(){
			try {
				super.deepCopy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public String toString() {
			return "patient [user_name=" + user_name + ", id=" + id
					+ ", num_recipes=" + num_recipes + ", num_remind="
					+ num_remind + ", logo=" + logo + ", reminds=" + reminds
					+ "]";
		}
		 
	};
	public class Reminds extends Result implements  Serializable{
		private static final long serialVersionUID = -8104470547305980861L;
		public String goods_name;
		public List<String> times;
		@Override
		public String toString() {
			return "Reminds [goods_name=" + goods_name + ", times=" + times
					+ "]";
		}
		public Reminds(){
			try {
				super.deepCopy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
