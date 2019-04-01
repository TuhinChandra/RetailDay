insert into question(question_ID,question_Text,option_A,option_B,option_C,option_D,option_Correct,current) values('1','Which UK based retailer (happens to be a TCS customer) was sponsor of English Cricket Team?','A. ASDA','B. Waitrose','C. M&S','D. Sainsbury','B. Waitrose',false);
insert into question(question_ID,question_Text,option_A,option_B,option_C,option_D,option_Correct,current) values('2','Kingfisher has an office in which Asian city?','A. Kolkata','B. Singapore','C. Hongkong','D. Tokyo','B. Singapore',false);
insert into question(question_ID,question_Text,option_A,option_B,option_C,option_D,option_Correct,current) values('3','Identify the person in the picture<br/><img src="Capture.JPG">','A. Russi Modi','B. Faquir Chand Kholi','C. Jamshedji Tata','D. Naval Tata','B. Faquir Chand Kholi (First CEO of TCS)',false);
insert into question(question_ID,question_Text,option_A,option_B,option_C,option_D,option_Correct,current) values('4','Name the player who representaed his country&quote;s national team in more than one sports.','A. Chuni Goswami','B. Jonty Rhodes','C. Diego Maradona','D. Nevil DSuza','B. Jonty Rhodes (Cricket & Hockey)',false);
insert into question(question_ID,question_Text,option_A,option_B,option_C,option_D,option_Correct,current) values('5','Calcutta Cup is the prize of which sports?','A. Footbal','B. Rugby','C. Hockey','D. Cricket','B. Rugby (match between England & Scotland)',false);

INSERT INTO event (event_id, event_name, event_details, start, end, state, ppt_path, photo, duration) VALUES
	(1, 'Industrial Revolution 4.0', 'Ani B', NULL, NULL, 0, NULL, 'fa fa-line-chart', '10 minutes'),
	(2, 'Address the KITSian', 'Sunandan Das', NULL, NULL, 0, NULL, 'fa fa-microphone', '10 minutes'),
	(3, 'TCS-Kingfisher Journey - A documentary', 'Movie', NULL, NULL, 0, NULL, 'fa fa-space-shuttle', '5 minutes'),
	(4, 'Stories of unknown Heroes', 'Ayon', NULL, NULL, 0, NULL, 'fa fa-trophy', '15 minutes'),
	(5, 'Grand launch of Bhuto', 'Sumangal & Team', NULL, NULL, 0, NULL, 'fa fa-bold', '15 minutes'),
	(6, 'Quiz', 'Prosenjit', NULL, NULL, 0, NULL, 'fa fa-question-circle', '25 minutes'),
	(7, 'Vishwakarma Consultancy Services - Strategic partner of Devlok', 'SKIT by Digital team', NULL, NULL, 0, NULL, 'fa fa-film', '12 minutes'),
	(8, 'Easier ka Gabbar kaun hai ?', 'SKIT by Easier team', NULL, NULL, 0, NULL, 'fa fa-film', '12 minutes'),
	(9, 'Evolution of automation - A documentary', 'SKIT by Testing Team', NULL, NULL, 0, NULL, 'fa fa-video-camera', '4 minutes'),
	(10, 'Do you want to buy a robot?', 'Preprod', NULL, NULL, 0, NULL, 'fa fa-film', '12 minutes'),
	(11, 'Ek din ke liye tester banoge?', 'SKIT by Testing Team', NULL, NULL, 0, NULL, 'fa fa-film', '12 minutes'),
	(12, 'Song', 'Safal Chetri', NULL, NULL, 0, NULL, 'fa fa-music', '5 minutes'),
	(13, 'Kabhi to humlogo ko enjoy karne do', 'Skit by IT IS', NULL, NULL, 0, NULL, 'fa fa-film', '12 minutes'),
	(14, 'No info available yet', 'Skit by AMS', NULL, NULL, 0, NULL, 'fa fa-film', '12 minutes');
