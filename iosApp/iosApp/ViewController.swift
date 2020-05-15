import UIKit
import shared

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate
{
    
    @IBOutlet weak var myTable: UITableView!
    
    var myArray: [Userx] = []
    
    let cellReuseIdentifier = "CELL"
    
    var limit: Int64 = 20
    var offset: Int = 0
    var totalEntries: Int = 100
    
    lazy var rcr: UIRefreshControl = {
        var rc = UIRefreshControl()
        rc.tintColor = .black
        rc.addTarget(self, action: #selector(requestData), for: .valueChanged)
        
        return rc
    }()
    
    @objc func requestData() {
        _resetdata(limit: limit)
        
        let deadline = DispatchTime.now() + .milliseconds(3000)
        
        DispatchQueue.main.asyncAfter(deadline: deadline) {
            self.rcr.endRefreshing()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        myTable.dataSource = self
        myTable.delegate = self
 
//        _insertdata(num: 7)
        _loaddata(offset: offset, limit: limit)
        
        
        myTable.refreshControl = rcr
    }
    
    private func _loaddata(offset: Int, limit: Int64)
    {
        let uDao = UserDao()
        let users = uDao.getUserList(offset: 0, limit: limit)
        
        users.forEach { user in
           // print(user)
            let u = Userx()
            u.name = user.name
            u.sex = user.sex.value == 1 ? "Nam" : "Nữ"
            u.avatar = user.avatar!
            myArray.append(u)
        }
    }
    
    private func _resetdata(limit: Int64)
    {
        let uDao = UserDao()
        let users = uDao.getUserList(offset: 0, limit: limit)
        myArray = []
        
        users.forEach { user in
           // print(user)
            let u = Userx()
            u.name = user.name
            u.sex = user.sex.value == 1 ? "Nam" : "Nữ"
            u.avatar = user.avatar!
            myArray.append(u)
        }
        self.perform(#selector(loadTable), with: nil, afterDelay: 1.5)
    }
    
    private func _insertdata(num: Int)
    {
        let uDao = UserDao()
        
        for i in 1 ... num {
            uDao.insertUserx(name: "Tran Huu HIen ios \(i)" , sex: "1")
            uDao.insertUserx(name: "Nguyen phong thuy ios \(i) ", sex: "1")
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       return myArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: cellReuseIdentifier)
        cell?.textLabel?.text = " \(indexPath.row).  Name: " + myArray[indexPath.row].name + ".  Sex: " + myArray[indexPath.row].sex
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60.0
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath){
        let lastElement = myArray.count - 1
        if indexPath.row == lastElement {
            if myArray.count < totalEntries {
                _loaddata(offset: myArray.count, limit: limit)
                self.perform(#selector(loadTable), with: nil, afterDelay: 1.5)
            }
        }
    }
    
    @objc func loadTable()
    {
        self.myTable.reloadData()
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBOutlet weak var label: UILabel!
}

