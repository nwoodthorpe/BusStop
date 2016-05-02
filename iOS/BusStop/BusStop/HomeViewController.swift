//
//  HomeViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class HomeViewController: UITableViewController {
    
    var stops = [savedStop]()
    let defaults = NSUserDefaults.standardUserDefaults()
    
    @IBAction func undwindToHome(segue: UIStoryboardSegue) {
        stops = [savedStop]()
        let array = defaults.objectForKey("savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for item in array {
            stops.append(Functions.dictToSavedStop(item))
        }
        tableView.reloadData()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        initLayout()
        initStops()

        NSTimer.scheduledTimerWithTimeInterval(60.0, target: self, selector: #selector(HomeViewController.updateTime), userInfo: nil, repeats: true)
        updateTime()
    }
    
    func initLayout() {
        navigationController?.navigationBar.tintColor = UIColor.whiteColor()
        navigationController?.navigationBar.barTintColor = UIColor(red: 33.0/255.0, green: 150.0/255.0, blue: 243.0/255.0, alpha: 1.0)
        navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.whiteColor()]
    
    }
    
    func initStops() {
        stops = [savedStop]()
        let array = defaults.objectForKey("savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for item in array {
            stops.append(Functions.dictToSavedStop(item))
        }
        tableView.reloadData()
    }
    
    func updateTime() {
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_UTILITY, 0)) { [unowned self] in
            for (index,currentStop) in self.stops.enumerate() {
                if let url = NSURL(string: "http://nwoodthorpe.com/grt/V2/livetime.php?stop=\(currentStop.stopNumber)"), contents = NSData(contentsOfURL: url) {

                    do {
                        let object = try NSJSONSerialization.JSONObjectWithData(contents, options: .AllowFragments)
                        if let dictionary = object as? [String: [AnyObject]] {
                            for element in dictionary["data"]! {
                                if currentStop.routeNumber == element["routeId"] as? NSString {
                                    self.stops[index].time = (element["departure"] as! NSNumber).integerValue - (element["time"] as! NSNumber).integerValue
                                }
                            }
                        }
                        else {
                            print("JSON failed to parse: Stop \(currentStop.nickname)")
                        }
                    }
                    catch {
                        print("handle error")
                    }
                }
            }
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                self.tableView.reloadData()
            }
        }
    }
    
    func setSwitch(routeName: String, stop: String, on: Bool) {
        for (index, currentStop) in stops.enumerate() {
            if currentStop.routeName == routeName && currentStop.stopNumber == stop {
                stops[index].on = on
                return
            }
        }
    }
    
    func save() {
        var array = [[String: AnyObject]]()
        for currentStop in stops {
            array.append(Functions.savedStopToDict(currentStop))
        }
        defaults.setObject(array, forKey: "savedStops")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return stops.count
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("SavedCell", forIndexPath: indexPath) as! SavedCell
        
        let currentStop = stops[indexPath.row]
        cell.initValues(currentStop.routeNumber, nickname: currentStop.nickname, routeName: currentStop.routeName, stopNumber: currentStop.stopNumber, time: currentStop.time, on: currentStop.on)
        cell.parent = self
        return cell
    }
    
    
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            stops.removeAtIndex(indexPath.row)
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
            
            save()
        } /*else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }*/ 
    }
    

    
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {
        let itemToMove = stops[fromIndexPath.row]
        stops.removeAtIndex(fromIndexPath.row)
        stops.insert(itemToMove, atIndex: toIndexPath.row)
        
        save()
    }
    

    
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
