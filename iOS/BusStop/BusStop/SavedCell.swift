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
    @IBAction func SwitchChanged(_ sender: AnyObject) {
        if !self.Switch.isOn {
            self.Time.text = "Off"
        }
        parent.setSwitch(routeName!, stop: stopNumber!, on: Switch.isOn)
        parent.save()
        let delayTime = DispatchTime.now() + Double(Int64(1 * Double(NSEC_PER_SEC))) / Double(NSEC_PER_SEC)
        DispatchQueue.main.asyncAfter(deadline: delayTime) { [unowned self] in
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
    
    func initValues(_ number: String, nickname: String, routeName: String, stopNumber: String, time: Int, on: Bool) {
        selectionStyle = UITableViewCellSelectionStyle.none
        RouteNumber.text = String(number)
        Nickname.text = nickname
        Switch.setOn(on, animated: false)
        if !Switch.isOn {
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

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
