# 🤖 University Booking bot

The project for the summer semester at Innopolis University

**📝 Project description**

The University Booking Bot is a bot designed to simplify the process of reserving meeting rooms at the university. This bot aims to streamline the booking experience for students, faculty, and staff members, making it more efficient and convenient.

**💼 Technological stack**

Backend
    
    - Java
        - Pengrad
        - Wisp
    - Docker
    - PostgreSQL

Frontend
    
    - JavaScript
    - React


# 👥 Team Members

—> [Efim Puzhalov](https://t.me/yeaphm) 

—> [Artur Mukhutdinov](https://t.me/CatOrLeader)

—> [Matvey Korinenko](https://t.me/m0t9_tg)

—> [Vladislav Grigorev](https://t.me/VLADISLAVVV777)

—> [Milana Sirozhova](https://t.me/milana_sirozhova)

# 🗣️ Customers
—> [Mike Kuskov](https://t.me/unaimillan)

—> [Andrei Markov](https://t.me/markovav_official)

# 🎥 Demo
—> [Link to the video-demo](https://drive.google.com/file/d/1b423BXN7o6m5BD7tpkNmLw-ZNgBF_vhw/view?usp=drive_link)


# ❓ How to use
1. In the dialog with bot press start.
2. Authorize via @innopolis email, entering a code from the message.
3. Now you can book the rooms: press the button, you will see the WebApp, enter here time and room you are going to book.
4. You can get your booking via button and delete ones.


# 🎇 Features list
- **📲 Integrated Web Application.** The bot provides a user-friendly interface that allows users to easily navigate and interact with the system. It offers a seamless booking experience with clear instructions and prompts.
- **❓ Meeting Room Availability.** The bot provides real-time information on the availability of meeting rooms at the university. Users can check the availability of rooms for specific dates and times, ensuring they can make informed decisions while planning their meetings.
- **🕹️ Booking Management.** Users can create, modify, and cancel meeting room bookings through the bot. The system allows users to specify the desired date, time, and duration requirements for their meetings. They can also add additional notes to the title.
- **📣 Notifications.** The bot sends automated notifications to users to confirm their bookings. This helps users stay updated and ensures minimisation of idle bookings.
- **⛔️ Access Control.** The bot incorporates an access control system that verifies the eligibility of users to book meeting rooms based on their permissions. This ensures that only authorized individuals can make reservations.


# ⬇️ Project installation
All the environment variables should be placed in the "docker-compose.yml" file.
1. Clone project from [GitLab](https://gitlab.pg.innopolis.university/ubb-cs-03-swp/ubb-cs-03.git)
2. Enter in the git bash (`bash --login`; If you don't have one, install it yourself.)
3. Make a stable connection with your **remote server** or **VM**
4. Copy the "dockersys" folder to the remote server. (Can be copied using scp, i.g. `scp -r ./dockersys username@ip:source/folder`)
5. Enter your remote server. (probably using the `ssh username@ip`). Move to the source/folder.
6. Start docker-compose (`docker-compose up -d`)
7. Copy the necessary files to the Docker containers:
    - `docker cp dockersys/DbBackups/IUBookingBotDb.backup db:/home`
8. Enter the DB container.: `docker exec -it db bash`
9. Use Container as a Postgres user: `su - postgres`
10. Restore backup: `pg_restore -U postgres -d IUBookingBotDb /home/IUBookingBotDb.backup`
11. Exit from the container and restart docker-compose: `docker-compose restart`

**🎉 Congratulations! You made it**. 

If you have any questions related to the deployment or resolving some other issues connected to the project, don't hesitate to ask any developer listed in the section **Team Members**.


# 🪪 License
The University Booking Bot is released under the [![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE). Feel free to use, modify, and distribute the bot as per the terms of the license.

# 🛎️ For customers

No specific requirements
