'use strict';

import React, { Component, StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import client from './client';

class App extends Component {

	constructor(props) {
		super(props);
		this.state = {
			year: '',
			rosebowls: [],
			roseBowlsByDate: [],
			overview: false,
			search: false
		};
	}

	componentDidMount() {
		const editModal = document.getElementById('editModal')
		editModal.addEventListener('show.bs.modal', function (event) {
			// Button that triggered the modal
			const button = event.relatedTarget;

			// Extract info from data-bs-* attributes
			const date = button.getAttribute('data-bs-date');
			const winner = button.getAttribute('data-bs-winner');
			const opponent = button.getAttribute('data-bs-opponent');

			const modalTitle = editModal.querySelector('.modal-title');
			const modalDateInput = editModal.querySelector('#date');
			const modalWinnerInput = editModal.querySelector('#winner');
			const modalOpponentInput = editModal.querySelector('#opponent');
		
			modalTitle.textContent = 'Edit Rose Bowl Game: ' + date;
			modalDateInput.value = date;
			modalWinnerInput.value = winner;
			modalOpponentInput.value = opponent;
		});
	}


	goToOverview() {
		this.setState({overview: true});
		this.fetchRoseBowlRecords();
	}

	searchRoseBowl() {
		if (this.state.year !== '') {
			client({method: 'GET', path: '/api/rosebowl/' + this.state.year}).then(response => {
				let roseBowlsByDate = [...this.state.roseBowlsByDate, response.entity];
				this.setState({ roseBowlsByDate });
			});
		}
	}

	fetchRoseBowlRecords() {
		client({method: 'GET', path: '/api/rosebowl/'}).then(response => {
			response.entity.map(rosebowl => {
				console.log("rosebowl ==== ", rosebowl);
			});
			let rosebowls = [...response.entity];
			this.setState({ rosebowls });
		});
	}

	updateRoseBowl() {
		const date = document.getElementById('date').value;
		const winner = document.getElementById('winner').value;
		const opponent = document.getElementById('opponent').value;
		
		client({
			method: 'PUT',
			path: '/api/rosebowl/' + date,
			entity: {'date': parseInt(date), 'winner': winner, 'opponent': opponent}
		})
		.then(response => {
			this.fetchRoseBowlRecords();
			console.log("data response ===== ", response);
		})
		.catch(error => console.log("PUT request failed ====", error));
	}

	render() {
		return (
			<div className='d-flex flex-column justify-content-center align-items-center'>
				<img
					src='https://i.pinimg.com/originals/ba/d7/62/bad76235a7db8df39fb2ceb7220f13ba.png'
					className='img-fluid my-5'
					alt='Rose Bowl Games Logo'
					style={{width: '40%'}}
				/>
				
				{(!this.state.overview && !this.state.search) &&
					<div className='d-flex justify-content-center row mt-5'>
						<div className='col-sm-4'>
							<div className='card'>
								<div className='card-body d-flex flex-column justify-content-center'>
									<h3 className='card-title mb-4 text-center'>Final Games Overview</h3>
									<h5 className='card-text mb-4 text-center'>Overview of Rose Bowl seasons' final game regardind year, winner and opponent</h5>
									<button type='button' className='btn btn-primary mx-5' onClick={() => this.goToOverview()}>Overview</button>
								</div>
							</div>
						</div>
						<div className='col-sm-4'>
							<div className='card'>
								<div className='card-body d-flex flex-column justify-content-center'>
									<h3 className='card-title mb-4 text-center'>Search by Year</h3>
									<h5 className='card-text mb-4 text-center'>Year, winner and opponent will be displayed according to the given year </h5>
									<button type='button' className='btn btn-primary mx-5' onClick={() => this.setState({search: true})}>Go Search</button>
								</div>
							</div>
						</div>
					</div>
				}

				{this.state.overview &&
				<React.Fragment>
					<button type='button' className='btn btn-primary my-5' onClick={() => this.setState({overview: false})}>Go Back</button>
					<RoseBowlList rosebowls={this.state.rosebowls}/>
				</React.Fragment>
				}

				{this.state.search &&
				<React.Fragment>
					<div className='input-group input-group-lg my-5' style={{width: '25%'}}>
						<span className='input-group-text' id='inputGroup-sizing-lg'>Search by Year</span>
						<input
							type='text'
							className='form-control'
							aria-label='Insert year'
							aria-describedby='inputGroup-sizing-lg'
							onChange={event => this.setState({year: event.target.value})}
						/>
					</div>

					<div className='d-flex flex-row mb-5'>
						<button type='button' className='btn btn-primary me-5' onClick={() => this.setState({search: false})}>Go Back</button>
						<button className='btn btn-primary' type='submit' onClick={() => this.searchRoseBowl()}>Submit</button>
					</div>

					<RoseBowlList rosebowls={this.state.roseBowlsByDate} search={this.state.search}/>
				</React.Fragment>
				}
				<div className='modal fade' id='editModal' aria-labelledby='editModalLabel' aria-hidden='true' tabIndex='-1'>
					<div className='modal-dialog'>
						<div className='modal-content'>
						<div className='modal-header'>
							<h5 className='modal-title'>Modal title</h5>
							<button type='button' className='btn-close' data-bs-dismiss='modal' aria-label='Close'></button>
						</div>
						<div className='modal-body'>
							<form>
							<div className='d-none mb-3'>
									<label htmlFor='date' className='col-form-label'>Date:</label>
									<input type='text' className='form-control' id='date'/>
								</div>
								<div className='mb-3'>
									<label htmlFor='winner' className='col-form-label'>Winner:</label>
									<input type='text' className='form-control' id='winner'/>
								</div>
								<div className='mb-3'>
									<label htmlFor='opponent' className='col-form-label'>Opponent:</label>
									<input type='text' className='form-control' id='opponent'/>
								</div>
							</form>
						</div>
						<div className='modal-footer'>
							<button type='button' className='btn btn-secondary' data-bs-dismiss='modal'>Close</button>
							<button type='button' className='btn btn-primary' data-bs-dismiss='modal' onClick={() => this.updateRoseBowl()}>Save changes</button>
						</div>
						</div>
					</div>
				</div>
			</div>
		)
	}
}

class RoseBowlList extends Component{
	render() {
		const rosebowls = this.props.rosebowls && this.props.rosebowls.map(rosebowl => {
			let randomKeyNumber = Math.random() * 1000;
			return Object.keys(rosebowl).length > 0 && <RoseBowl key={`${randomKeyNumber}-${rosebowl.date}`} rosebowl={rosebowl} search={this.props.search}/>
		});

		return (
				<table className='table table-dark table-striped align-self-center' style={{width: '50%'}}>
					<tbody>
						<tr className='table-info text-center'>
							<th>Date</th>
							<th>Winner</th>
							<th>Opponent</th>
						</tr>
						{rosebowls}
					</tbody>
				</table>
		)
	}
}

class RoseBowl extends Component{
	render() {
		return (
			<tr>
				<td className='col-1 text-center'>{this.props.rosebowl.date}</td>
				<td className='col-1 text-center'>{this.props.rosebowl.winner}</td>
				<td className='col-1 text-center'>{this.props.rosebowl.opponent}</td>
				{!this.props.search && <td className='col-1 text-center'>
					<button
						type='button'
						className='btn btn-primary'
						data-bs-toggle='modal'
						data-bs-target='#editModal'
						data-bs-date={this.props.rosebowl.date}
						data-bs-winner={this.props.rosebowl.winner}
						data-bs-opponent={this.props.rosebowl.opponent}
					>
						Edit
					</button>
				</td>}
			</tr>
		)
	}
}

const rootElement = document.getElementById('root');
const root = createRoot(rootElement);

root.render(
	<StrictMode>
	  <App />
	</StrictMode>
  );