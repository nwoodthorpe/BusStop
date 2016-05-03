//
//  FinishViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-29.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class FinishViewController: UITableViewController {

    @IBOutlet weak var routeLabel: UILabel!
    @IBOutlet weak var stopLabel: UILabel!
    @IBOutlet weak var nameField: UITextField!
    @IBAction func doneButton(sender: AnyObject) {
    }
    
    var stop: Stop?
    let defaults = NSUserDefaults.standardUserDefaults()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initLabels()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        title = "Add"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func initLabels() {
        routeLabel.text = "\(stop!.routeNumber) - \(stop!.routeName)"
        stopLabel.text = "\(stop!.stopNumber) - \(stop!.stopName)"
    }
    
    func save() {
        var newStop: savedStop
        if nameField.text! == "" {
            newStop = savedStop(stop: stop!, nickname:stop!.stopName)
        } else {
            newStop = savedStop(stop: stop!, nickname: nameField.text!)
        }

        var array = defaults.objectForKey("savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for (index, element) in array.enumerate() {
            let item = Functions.dictToSavedStop(element)
            
            if item.stopNumber == newStop.stopNumber && item.routeNumber == newStop.routeNumber {
                array[index]["nickname"] = newStop.nickname
                array[index]["on"] = true
                defaults.setObject(array, forKey: "savedStops")
                return
            }
        }
        array.append(Functions.savedStopToDict(newStop))
        defaults.setObject(array, forKey: "savedStops")
        return
    }

    // MARK: - Table view data source
    /*
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return 0
    }
    */
    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ExitSegue" {
            save()
        }
    }
    

}
