//
//  TodayViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-05-08.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit
import NotificationCenter
import BusKit

class TodayViewController: UITableViewController, NCWidgetProviding {
    
    let defaults = NSUserDefaults(suiteName: "group.me.harryliu.BusStop")!
    var stops = [savedStop]()

    override func viewDidLoad() {
        super.viewDidLoad()
        initStops()
        tableView.allowsSelection = false
        updatePreferredContentSize()
        tableView.reloadData()
        print("will update")
        update()
    }
    
    func initStops() {
        stops = [savedStop]()
        let array = defaults.objectForKey("savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for item in array {
            stops.append(Functions.dictToSavedStop(item))
        }
    }
    
    func widgetPerformUpdateWithCompletionHandler(completionHandler: ((NCUpdateResult) -> Void)) {
        // Perform any setup necessary in order to update the view.
        
        // If an error is encountered, use NCUpdateResult.Failed
        // If there's no update required, use NCUpdateResult.NoData
        // If there's an update, use NCUpdateResult.NewData
        
        completionHandler(NCUpdateResult.NewData)
    }
    
    func widgetMarginInsetsForProposedMarginInsets
        (defaultMarginInsets: UIEdgeInsets) -> (UIEdgeInsets) {
        return UIEdgeInsetsZero
    }
    
    func updatePreferredContentSize() {
        preferredContentSize = CGSizeMake(CGFloat(0), CGFloat(tableView(tableView, numberOfRowsInSection: 0)) * CGFloat(tableView.rowHeight) + tableView.sectionFooterHeight)
    }
    /*
    override func viewWillTransitionToSize(size: CGSize, withTransitionCoordinator coordinator: UIViewControllerTransitionCoordinator) {
        coordinator.animateAlongsideTransition({ context in
            self.tableView.frame = CGRectMake(0, 0, size.width, size.height)
            }, completion: nil)
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
        let cell = tableView.dequeueReusableCellWithIdentifier("TodayCell", forIndexPath: indexPath) as! TodayCell
        let stop = stops[indexPath.row]
        cell.Nickname.text = stop.nickname
        if stop.time < 0 {
            cell.Time.text = "Loading time"
        }
        else {
            cell.Time.text = "\(stop.time / 60) minutes"
        }
        cell.RouteNumber.text = stop.routeNumber
        // Configure the cell...
        

        return cell
    }
    
    func update() {
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_USER_INITIATED, 0)) { [unowned self] in
            self.stops = Functions.update(self.stops)
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                self.tableView.reloadData()
                print("did update")
            }
        }
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