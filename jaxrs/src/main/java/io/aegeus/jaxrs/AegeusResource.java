package io.aegeus.jaxrs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nessus.Wallet.Address;
import io.nessus.ipfs.ContentManager;
import io.nessus.ipfs.FHandle;
import io.nessus.utils.AssertState;

public class AegeusResource implements AegeusEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(AegeusResource.class);
    
    final ContentManager cntmgr;

    public AegeusResource() throws IOException {

        AegeusApplication app = AegeusApplication.getInstance();
        cntmgr = app.getContentManager();
    }

    @Override
    public String register(String rawAddr) throws GeneralSecurityException {

        Address addr = assertWalletAddress(rawAddr);

        PublicKey pubKey = cntmgr.register(addr);
        String encKey = Base64.getEncoder().encodeToString(pubKey.getEncoded());
        LOG.info("/register => {}", encKey);

        return encKey;
    }

    @Override
    public SFHandle add(String rawAddr, String path, InputStream input) throws IOException, GeneralSecurityException {

        Address owner = assertWalletAddress(rawAddr);
        FHandle fhandle = cntmgr.add(owner, input, Paths.get(path));

        AssertState.assertTrue(new File(fhandle.getURL().getPath()).exists());
        AssertState.assertNotNull(fhandle.getCid());

        SFHandle shandle = new SFHandle(fhandle);
        LOG.info("/add => {}", shandle);
        
        return shandle;
    }

    @Override
    public SFHandle get(String rawAddr, String cid, String path, Long timeout) throws IOException, GeneralSecurityException {

        Address owner = assertWalletAddress(rawAddr);
        FHandle fhandle = cntmgr.get(owner, cid, Paths.get(path), timeout);

        AssertState.assertTrue(new File(fhandle.getURL().getPath()).exists());
        AssertState.assertNull(fhandle.getCid());

        SFHandle shandle = new SFHandle(fhandle);
        LOG.info("/get => {}", shandle);
        
        return shandle;
    }

    @Override
    public SFHandle send(String rawAddr, String cid, @QueryParam("target") String rawTarget, Long timeout) throws IOException, GeneralSecurityException {

        Address owner = assertWalletAddress(rawAddr);
        Address target = assertWalletAddress(rawTarget);

        FHandle fhandle = cntmgr.send(owner, cid, target, timeout);
        AssertState.assertNotNull(fhandle.getCid());

        SFHandle shandle = new SFHandle(fhandle);
        LOG.info("/send => {}", shandle);
        
        return shandle;
    }

    @Override
    public String findRegistation(String rawAddr) {

        Address addr = assertWalletAddress(rawAddr);
        PublicKey pubKey = cntmgr.findRegistation(addr);
        
        String encKey = pubKey != null ? Base64.getEncoder().encodeToString(pubKey.getEncoded()) : null;
        LOG.info("/findkey => {}", encKey);
        
        return encKey;
    }

    @Override
    public List<SFHandle> findIPFSContent(String rawAddr, Long timeout) throws IOException {

        List<SFHandle> result = new ArrayList<>();

        Address owner = assertWalletAddress(rawAddr);
        for (FHandle fhandle : cntmgr.findIPFSContent(owner, timeout)) {
            result.add(new SFHandle(fhandle));
        }
        LOG.info("/findipfs => {}", result);

        return result;
    }

    @Override
    public List<SFHandle> findLocalContent(String rawAddr) throws IOException {

        List<SFHandle> result = new ArrayList<>();

        Address owner = assertWalletAddress(rawAddr);
        for (FHandle fhandle : cntmgr.findLocalContent(owner)) {
            result.add(new SFHandle(fhandle));
        }
        LOG.info("/findlocal => {}", result);

        return result;
    }

    @Override
    public InputStream getLocalContent(String rawAddr, String path) throws IOException {

        Address owner = assertWalletAddress(rawAddr);
        InputStream content = cntmgr.getLocalContent(owner, Paths.get(path));
        LOG.info("/getlocal => {}", content);
        
        return content;
    }

    @Override
    public boolean deleteLocalContent(String rawAddr, String path) throws IOException {

        Address owner = assertWalletAddress(rawAddr);
        boolean deleted = cntmgr.deleteLocalContent(owner, Paths.get(path));
        LOG.info("/dellocal => {}", deleted);
        
        return deleted;
    }

    private Address assertWalletAddress(String rawAddr) {
        Address addr = cntmgr.getBlockchain().getWallet().findAddress(rawAddr);
        AssertState.assertNotNull(addr, "Unknown address: " + rawAddr);
        return addr;
    }
}