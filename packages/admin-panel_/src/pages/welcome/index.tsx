import Welcome from '../../assets/images/welcome.png'

const WelcomePage = () => {
  return (
    <div style={{height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
      <img src={Welcome} alt="" style={{width: '70%', display: 'flex', margin: 'auto'}} />
    </div>
  )
}

export default WelcomePage;