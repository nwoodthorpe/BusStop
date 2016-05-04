//
//  SavedCell.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class SavedCell: UITableViewCell {

    @IBOutlet weak var RouteNumber: UILabel!
    @IBOutlet weak var Nickname: UILabel!
    @IBOutlet weak var Time: UILabel!
    @IBOutlet weak var Switch: UISwitch!
    @IBAction func SwitchChanged(sender: AnyObject) {
        if !self.Switch.on {
            self.Time.text = "Off"
        }
        parent.setSwitch(routeName!, stop: stopNumber!, on: Switch.on)
        parent.save()
        let delayTime = dispatch_time(DISPATCH_TIME_NOW, Int64(1 * Double(NSEC_PER_SEC)))
        dispatch_after(delayTime, dispatch_get_main_queue()) { [unowned self] in
            self.parent.tableView.reloadData()
        }
    }
    @IBOutlet weak var parent: HomeViewController!
    
    var routeName: String?
    var stopNumber: String?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        initCircle()
    }
    
    func initCircle() {
        RouteNumber.layer.borderWidth = 3
        RouteNumber.layer.cornerRadius = RouteNumber.frame.width / 2
    }
    
    func initValues(number: String, nickname: String, routeName: String, stopNumber: String, time: Int, on: Bool) {
        selectionStyle = UITableViewCellSelectionStyle.None
        RouteNumber.text = String(number)
        Nickname.text = nickname
        Switch.setOn(on, animated: false)
        if !Switch.on {
            Time.text = "Off"
        }
        else if time < 0 {
            Time.text = "Loading time"
        }
        else {
            Time.text = "\(time / 60) minutes"
        }
        self.routeName = routeName
        self.stopNumber = stopNumber
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
