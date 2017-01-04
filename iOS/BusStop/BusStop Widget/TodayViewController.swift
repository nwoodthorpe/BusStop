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
    
    let defaults = UserDefaults(suiteName: "group.me.harryliu.BusStop")!
    var stops = [savedStop]()

    override func viewDidLoad() {
        super.viewDidLoad()
        initStops()
        tableView.allowsSelection = false
        updatePreferredContentSize()
        tableView.reloadData()
        update()
    }
    
    func initStops() {
        stops = [savedStop]()
        let array = defaults.object(forKey: "savedStops") as? [[String: AnyObject]] ?? [[String: AnyObject]]()
        for item in array {
            if (item["on"] as! Bool) {
                stops.append(Functions.dictToSavedStop(item))
            }
        }
    }
    
    func widgetPerformUpdate(completionHandler: (@escaping (NCUpdateResult) -> Void)) {
        // Perform any setup necessary in order to update the view.
        
        // If an error is encountered, use NCUpdateResult.Failed
        // If there's no update required, use NCUpdateResult.NoData
        // If there's an update, use NCUpdateResult.NewData
        
        completionHandler(NCUpdateResult.newData)
    }
    
    func widgetMarginInsets
        (forProposedMarginInsets defaultMarginInsets: UIEdgeInsets) -> (UIEdgeInsets) {
        return UIEdgeInsets.zero
    }
    
    func updatePreferredContentSize() {
        preferredContentSize = CGSize(width: CGFloat(0), height: CGFloat(tableView(tableView, numberOfRowsInSection: 0)) * CGFloat(tableView.rowHeight) + tableView.sectionFooterHeight)
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

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return stops.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TodayCell", for: indexPath) as! TodayCell
        let stop = stops[indexPath.row]
        cell.Nickname.text = stop.nickname
        if stop.time < 0 {
            cell.Time.text = "Loading"
        }
        else {
            cell.Time.text = "\(stop.time / 60) minutes"
        }
        cell.RouteNumber.text = stop.routeNumber

        return cell
    }
    
    func update() {
        DispatchQueue.global(qos: DispatchQoS.QoSClass.userInitiated).async { [unowned self] in
            self.stops = Functions.update(self.stops)
            DispatchQueue.main.async { [unowned self] in
                self.tableView.reloadData()
            }
        }
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
