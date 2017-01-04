//
//  RouteCell.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class RouteCell: UITableViewCell {

    @IBOutlet weak var RouteNumber: UILabel!
    @IBOutlet weak var RouteName: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        initCircle()
    }
    
    func initCircle() {
        RouteNumber.layer.borderWidth = 3
        RouteNumber.layer.cornerRadius = RouteNumber.frame.width / 2
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
}
