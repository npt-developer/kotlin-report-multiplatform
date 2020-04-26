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
    
    let cellReuseIdentifier = "CELL"
    
    var db:DBHelper = DBHelper()
    
    var persons:[Person] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        myTable.dataSource = self
        
        myTable.register(UITableViewCell.self, forCellReuseIdentifier: cellReuseIdentifier)
        db.insert(id: 1, name: "Tran Huu Hien", age: 30)
        db.insert(id: 2, name: "Nguyen Phong Thuy", age: 25)
        persons = db.read()
 
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //return 5
       return persons.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //tableView.reloadData()
        let cell = tableView.dequeueReusableCell(withIdentifier: cellReuseIdentifier)
        cell?.textLabel?.text = "Name: " + persons[indexPath.row].name + ", " + "Age: " + String(persons[indexPath.row].age)
        //cell?.textLabel?.text = "Hello"
        
        return cell!
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBOutlet weak var label: UILabel!
}

