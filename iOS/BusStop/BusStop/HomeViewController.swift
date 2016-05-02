//
//  HomeViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class HomeViewController: UITableViewController {
    
    var stops = [Stop]()
    var timer: dispatch_source_t!
    
    @IBAction func undwindToHome(segue: UIStoryboardSegue) {
        stops = [Stop]()
        let defaults = NSUserDefaults.standardUserDefaults()
        let array = defaults.objectForKey("savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for item in array {
            stops.append(Functions.dictToStop(item))
        }
        tableView.reloadData()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        initStops()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        NSTimer.scheduledTimerWithTimeInterval(60.0, target: self, selector: "updateTime", userInfo: nil, repeats: true)
        updateTime()
    }
    
    func initStops() {
        stops = [Stop]()
        let defaults = NSUserDefaults.standardUserDefaults()
        let array = defaults.objectForKey("savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for item in array {
            stops.append(Functions.dictToStop(item))
        }
        tableView.reloadData()
    }
    
    func updateTime() {
        print("timer working")
        for (index,currentStop) in stops.enumerate() {
            if let url = NSURL(string: "http://nwoodthorpe.com/grt/V2/livetime.php?stop=\(currentStop.stopNumber)") {
                if let contents = NSData(contentsOfURL: url) {
                    do {
                        let object = try NSJSONSerialization.JSONObjectWithData(contents, options: .AllowFragments)
                        //print(object)
                        if let dictionary = object as? [String: [AnyObject]] {
                            for element in dictionary["data"]! {
                                if currentStop.routeNumber == (element["routeId"] as! NSString).integerValue {
                                    stops[index].time = (element["departure"] as! NSNumber).integerValue - (element["time"] as! NSNumber).integerValue
                                    //print("new time \(stops[index].time)")
                                }
                            }
                        } else {
                            print("JSON failed to parse: Stop \(currentStop.nickname)")
                        }
                    } catch {
                        print("handle error")
                    }

                }
            }
        }
        tableView.reloadData()
    }
    /*
    func initJSON() {
        let url = NSBundle.mainBundle().URLForResource("stops", withExtension: "json")
        let data = NSData(contentsOfURL: url!)
        
        do {
            let object = try NSJSONSerialization.JSONObjectWithData(data!, options: .AllowFragments)
            if let dictionary = object as? [AnyObject] {
                readJSONObject(dictionary)
            } else {
                print("initJSON failed to parse JSON")
            }
        } catch {
            print("Handle error")
        }
    }
    */
    
    /*
    func startTimer() {
        let queue = dispatch_queue_create("com.domain.app.timer", nil)
        timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue)
        dispatch_source_set_timer(timer, DISPATCH_TIME_NOW, 60 * NSEC_PER_SEC, 1 * NSEC_PER_SEC) // every 60 seconds, with leeway of 1 second
        dispatch_source_set_event_handler(timer) {
            // do whatever you want here
        }
        dispatch_resume(timer)
    }
    
    func stopTimer() {
        dispatch_source_cancel(timer)
        timer = nil
    }*/

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
        
        let stop = stops[indexPath.row]
        cell.initValues(number: stop.routeNumber, name: stop.nickname, time: stop.time)

        return cell
    }
    
    
    

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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
