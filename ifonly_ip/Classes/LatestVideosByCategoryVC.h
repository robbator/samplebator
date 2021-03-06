#import <UIKit/UIKit.h>
#import "VCBase.h"
#import "GData.h"
#import "LatestVideosByCategoryTC.h"

@interface LatestVideosByCategoryVC : VCBase <UISearchBarDelegate> {
	NSString* category;
	GDataServiceGoogleYouTube* ytService;
	IBOutlet UITableView* tableView;
	LatestVideosByCategoryTC* tableVC;
	IBOutlet UISegmentedControl* orderBy;
	IBOutlet UISearchBar* searchBar;
	IBOutlet UIActivityIndicatorView* actIndView;
	IBOutlet UIButton* searchBtn;
	IBOutlet UILabel* noVidsLbl;
	UIImageView* catImgView;
}

@property (nonatomic, retain) UIImageView* catImgView;
@property (nonatomic, retain) NSString* category;
@property (nonatomic, retain) GDataServiceGoogleYouTube* ytService;
@property (nonatomic, retain) IBOutlet UITableView* tableView;
@property (nonatomic, retain) LatestVideosByCategoryTC* tableVC;
@property (nonatomic, retain) IBOutlet UISegmentedControl* orderBy;
@property (nonatomic, retain) IBOutlet UISearchBar* searchBar;
@property (nonatomic, retain) IBOutlet UIActivityIndicatorView* actIndView;
@property (nonatomic, retain) IBOutlet UIButton* searchBtn;
@property (nonatomic, retain) IBOutlet UILabel* noVidsLbl;

- (void)entryListFetchTicket: (GDataServiceTicket *)ticket finishedWithFeed: (GDataFeedBase *)feed error: (NSError*) error;
- (IBAction)searchAction;

@end
