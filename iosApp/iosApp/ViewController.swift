//
//  ViewController.swift
//  iosApp
//
//  Created by jetbrains on 12/04/2018.
//  Copyright © 2018 JetBrains. All rights reserved.
//

import UIKit
import shared

class ViewController: UIViewController, UITableViewDataSource
{
    

    @IBOutlet weak var myTable: UITableView!
    
    var myArray: [Userx] = []
    
    let cellReuseIdentifier = "CELL"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        myTable.dataSource = self
        
        let uDao = UserDao()
        uDao.insertUserx(name: "Tran Huu HIen ios", sex: "1")
        uDao.insertUserx(name: "Nguyen phong thuy ios", sex: "1")
        
        let users = uDao.getUserList(offset: 0, limit: 1000)
        
        users.forEach { user in
            print(user)
            let u = Userx()
            u.name = user.name
            u.sex = user.sex.value == 1 ? "Nam" : "Nữ"
            u.avatar = user.avatar!
            myArray.append(u)
        }
 
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       return myArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: cellReuseIdentifier)
        cell?.textLabel?.text = "Name: " + myArray[indexPath.row].name + ".  Sex: " + myArray[indexPath.row].sex
        
        return cell!
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBOutlet weak var label: UILabel!
}

